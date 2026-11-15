package com.varicent.common.config.web;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class QueryStringParameterPresencePredicate implements HttpServletRequestResponsePredicate {
	Set<String> parameterNames;

	public QueryStringParameterPresencePredicate(Collection<String> parameterNames) {
		this.parameterNames = new HashSet(parameterNames);
	}

	@Override
	public boolean test(HttpServletRequest request, HttpServletResponse response) {
		Set<String> requestParameters = request.getParameterMap().keySet();
		return requestParameters.containsAll(this.parameterNames);
	}
}
