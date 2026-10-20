package com.varicent.security.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class SecurityHeadersFilter implements Filter {

	static final String DEFAULT_HSTS = "max-age=63072000; includeSubDomains";
	static final String DEFAULT_FRAME_OPTIONS = "SAMEORIGIN";
	static final String DEFAULT_CONTENT_TYPE_OPTIONS = "nosniff";
	static final String DEFAULT_REFERRER_POLICY = "no-referrer-when-downgrade";
	static final String DEFAULT_PERMISSIONS_POLICY = "camera=(), geolocation=(), microphone=(), usb=(), payment=()";


	static final String DEFAULT_CSP = "default-src 'self'; " + "script-src 'self' 'unsafe-inline' 'unsafe-eval'; "
			+ "style-src 'self' 'unsafe-inline'; " + "img-src 'self' data:; " + "font-src 'self'; "
			+ "connect-src 'self'; " + "object-src 'none'; " + "base-uri 'self'; " + "form-action 'self'; "
			+ "frame-ancestors 'self'";


	private static final Set<String> LOCKED_HEADERS = Collections.unmodifiableSet(new HashSet<>(
			Arrays.asList("strict-transport-security", "x-frame-options", "x-content-type-options", "referrer-policy",
					"permissions-policy", "content-security-policy", "x-content-security-policy", "x-powered-by")));

	private String hsts;
	private String frameOptions;
	private String contentTypeOptions;
	private String referrerPolicy;
	private String permissionsPolicy;
	private String contentSecurityPolicy;
	private boolean alwaysSendHsts;

	@Override
	public void init(FilterConfig config) throws ServletException {

		this.alwaysSendHsts = Boolean.parseBoolean(config.getInitParameter("alwaysSendHsts"));
		this.hsts = param(config, "strictTransportSecurity", DEFAULT_HSTS);
		this.frameOptions = param(config, "frameOptions", DEFAULT_FRAME_OPTIONS);
		this.contentTypeOptions = param(config, "contentTypeOptions", DEFAULT_CONTENT_TYPE_OPTIONS);
		this.referrerPolicy = param(config, "referrerPolicy", DEFAULT_REFERRER_POLICY);
		this.permissionsPolicy = param(config, "permissionsPolicy", DEFAULT_PERMISSIONS_POLICY);
		this.contentSecurityPolicy = param(config, "contentSecurityPolicy", DEFAULT_CSP);
	}

	private static String param(FilterConfig config, String name, String defaultValue) {
		String value = config.getInitParameter(name);
		return value == null ? defaultValue : value.trim();
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		if (servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			HttpServletResponse response = (HttpServletResponse) servletResponse;

			if (this.alwaysSendHsts || request.isSecure()) {
				set(response, "Strict-Transport-Security", this.hsts);
			}

			set(response, "X-Frame-Options", this.frameOptions);
			set(response, "X-Content-Type-Options", this.contentTypeOptions);
			set(response, "Referrer-Policy", this.referrerPolicy);
			set(response, "Permissions-Policy", this.permissionsPolicy);

			set(response, "Content-Security-Policy", this.contentSecurityPolicy);

			set(response, "X-Content-Security-Policy", this.contentSecurityPolicy);

			response.setHeader("X-Powered-By", "");

			chain.doFilter(servletRequest, lock(response));
		} else {
			chain.doFilter(servletRequest, servletResponse);
		}
	}

	private static HttpServletResponse lock(HttpServletResponse response) {
		return new HttpServletResponseWrapper(response) {
			@Override
			public void setHeader(String name, String value) {
				if (!isLocked(name)) {
					super.setHeader(name, value);
				}
			}

			@Override
			public void addHeader(String name, String value) {
				if (!isLocked(name)) {
					super.addHeader(name, value);
				}
			}
		};
	}

	private static boolean isLocked(String name) {
		return name != null && LOCKED_HEADERS.contains(name.toLowerCase(Locale.ENGLISH));
	}

	private static void set(HttpServletResponse response, String name, String value) {
		if (value != null && !value.isEmpty()) {
			response.setHeader(name, value);
		}
	}

	@Override
	public void destroy() {
	}
}
