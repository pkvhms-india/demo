package com.varicent.common.context.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class CachingContextInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		this.setCachingResponseHeaders(request, response);
		return super.preHandle(request, response, handler);
	}

	public void setCachingResponseHeaders(HttpServletRequest request, HttpServletResponse response) {
		String browser = request.getHeader("User-Agent");
		String url = request.getRequestURL().toString();
		if (browser.indexOf("MSIE") > -1 && !url.endsWith("/login.html")) {
			response.setHeader("Cache-Control", "max-age=0");
		} else {
			response.setHeader("Cache-Control", "no-cache");
		}

		response.setHeader("Pragma", "no-cache");
	}
}
