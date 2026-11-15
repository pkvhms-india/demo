package com.varicent.common.config.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NotRequestPredicate implements HttpServletRequestResponsePredicate {
	HttpServletRequestResponsePredicate subPredicate;

	public NotRequestPredicate(HttpServletRequestResponsePredicate subPredicate) {
		this.subPredicate = subPredicate;
	}

	@Override
	public boolean test(HttpServletRequest request, HttpServletResponse response) {
		return !this.subPredicate.test(request, response);
	}
}
