package com.varicent.security.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class CookieHardeningFilter implements Filter {

	private static final String SET_COOKIE = "Set-Cookie";

	private static final String SESSION_COOKIE = "JSESSIONID";

	private static boolean isSessionCookie(String setCookieValue) {
		if (setCookieValue == null) {
			return false;
		}
		String trimmed = setCookieValue.trim();
		int equals = trimmed.indexOf('=');
		String name = equals < 0 ? trimmed : trimmed.substring(0, equals);
		return SESSION_COOKIE.equalsIgnoreCase(name.trim());
	}

	private String sameSite;
	private boolean forceSecure;

	@Override
	public void init(FilterConfig config) throws ServletException {
		String configuredSameSite = config.getInitParameter("sameSite");
		this.sameSite = configuredSameSite == null ? "Lax" : configuredSameSite.trim();
		this.forceSecure = Boolean.parseBoolean(config.getInitParameter("forceSecure"));
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		if (!(servletRequest instanceof HttpServletRequest) || !(servletResponse instanceof HttpServletResponse)) {
			chain.doFilter(servletRequest, servletResponse);
			return;
		}

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		final boolean secure = this.forceSecure || request.isSecure();

		HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(response) {
			@Override
			public void addCookie(Cookie cookie) {
				if (cookie != null && SESSION_COOKIE.equalsIgnoreCase(cookie.getName())) {
					super.addCookie(cookie);
					return;
				}
				cookie.setHttpOnly(true);
				cookie.setSecure(secure);
				super.addHeader(SET_COOKIE, harden(toHeaderValue(cookie), secure));
			}

			@Override
			public void addHeader(String name, String value) {
				super.addHeader(name,
						SET_COOKIE.equalsIgnoreCase(name) && !isSessionCookie(value) ? harden(value, secure) : value);
			}

			@Override
			public void setHeader(String name, String value) {
				super.setHeader(name,
						SET_COOKIE.equalsIgnoreCase(name) && !isSessionCookie(value) ? harden(value, secure) : value);
			}
		};

		chain.doFilter(request, wrapper);

		if (!response.isCommitted()) {
			Collection<String> existing = response.getHeaders(SET_COOKIE);
			if (existing != null && !existing.isEmpty()) {
				List<String> rewritten = new ArrayList<>(existing.size());
				boolean changed = false;
				for (String value : existing) {
					String hardened = isSessionCookie(value) ? value : harden(value, secure);
					changed |= !hardened.equals(value);
					rewritten.add(hardened);
				}
				if (changed) {
					boolean first = true;
					for (String value : rewritten) {
						if (first) {
							response.setHeader(SET_COOKIE, value);
							first = false;
						} else {
							response.addHeader(SET_COOKIE, value);
						}
					}
				}
			}
		}
	}

	private String harden(String setCookieValue, boolean secure) {
		if (setCookieValue == null || setCookieValue.isEmpty()) {
			return setCookieValue;
		}
		String lower = setCookieValue.toLowerCase(Locale.ENGLISH);
		StringBuilder value = new StringBuilder(setCookieValue);
		if (secure && !containsAttribute(lower, "secure")) {
			value.append("; Secure");
		}
		if (!containsAttribute(lower, "httponly")) {
			value.append("; HttpOnly");
		}
		if (!this.sameSite.isEmpty() && !lower.contains("samesite=")) {
			if (!"none".equalsIgnoreCase(this.sameSite) || secure) {
				value.append("; SameSite=").append(this.sameSite);
			}
		}
		return value.toString();
	}

	private static boolean containsAttribute(String lowerCasedHeader, String attribute) {
		int from = 0;
		while (true) {
			int index = lowerCasedHeader.indexOf(attribute, from);
			if (index < 0) {
				return false;
			}
			int before = index - 1;
			while (before >= 0 && lowerCasedHeader.charAt(before) == ' ') {
				before--;
			}
			int after = index + attribute.length();
			boolean startsAttribute = before >= 0 && lowerCasedHeader.charAt(before) == ';';
			boolean endsAttribute = after >= lowerCasedHeader.length() || lowerCasedHeader.charAt(after) == ';'
					|| lowerCasedHeader.charAt(after) == ' ';
			if (startsAttribute && endsAttribute) {
				return true;
			}
			from = index + attribute.length();
		}
	}

	private static String toHeaderValue(Cookie cookie) {
		StringBuilder header = new StringBuilder();
		header.append(cookie.getName()).append('=').append(cookie.getValue() == null ? "" : cookie.getValue());
		if (cookie.getPath() != null) {
			header.append("; Path=").append(cookie.getPath());
		}
		if (cookie.getDomain() != null) {
			header.append("; Domain=").append(cookie.getDomain());
		}
		if (cookie.getMaxAge() >= 0) {
			header.append("; Max-Age=").append(cookie.getMaxAge());
		}
		return header.toString();
	}

	@Override
	public void destroy() {
	}
}
