package com.varicent.common.config.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.util.matcher.RequestMatcher;

public class RequestMatchingPredicate implements HttpServletRequestResponsePredicate {
	RequestMatcher matcher;

	public RequestMatchingPredicate(RequestMatcher matcher) {
		this.matcher = matcher;
	}

	@Override
	public boolean test(HttpServletRequest request, HttpServletResponse response) {
		return this.matcher.matches(request);
	}
}
