package com.varicent.common.concurrency.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.varicent.common.concurrency.service.ConcurrencyManager;
import com.varicent.core.exceptions.GlobalLockException;

@Component
public class GlobalLockInterceptor extends HandlerInterceptorAdapter {
	private final ConcurrencyManager concurrencyManager;

	@Autowired
	public GlobalLockInterceptor(ConcurrencyManager concurrencyManager) {
		this.concurrencyManager = concurrencyManager;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (this.concurrencyManager.isGlobalLocked()) {
			throw new GlobalLockException();
		} else {
			return super.preHandle(request, response, handler);
		}
	}
}
