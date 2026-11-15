package com.varicent.common.config.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.varicent.common.config.service.ConfigManager;
import com.varicent.core.exceptions.WebDisabledException;

@Component
public class WebStatusInterceptor extends HandlerInterceptorAdapter {
	private static final String WEB_ENABLED_ATTRIBUTE = "webEnabled";
	private final ConfigManager configManager;

	@Autowired
	public WebStatusInterceptor(ConfigManager configManager) {
		this.configManager = configManager;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		boolean enabled = !this.configManager.webEnabledExists() || this.configManager.isWebEnabled();
		request.setAttribute("webEnabled", enabled);
		if (!enabled) {
			throw new WebDisabledException();
		} else {
			return super.preHandle(request, response, handler);
		}
	}
}
