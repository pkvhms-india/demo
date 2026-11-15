package com.varicent.common.config.web;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AndRequestPredicate implements HttpServletRequestResponsePredicate {
	Set<HttpServletRequestResponsePredicate> subPredicates;

	public AndRequestPredicate(Collection<HttpServletRequestResponsePredicate> subPredicates) {
		this.subPredicates = new HashSet(subPredicates);
	}

	@Override
	public boolean test(HttpServletRequest request, HttpServletResponse response) {
		for (HttpServletRequestResponsePredicate p : this.subPredicates) {
			if (!p.test(request, response)) {
				return false;
			}
		}

		return true;
	}
}
