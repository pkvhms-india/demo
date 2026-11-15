package com.varicent.common.config.web;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class HttpServletRequestResponsePredicateMatcher<T> {
	Map<HttpServletRequestResponsePredicate, T> conditionsToResults;
	Logger logger;
	T defaultResult;

	public HttpServletRequestResponsePredicateMatcher(Map<HttpServletRequestResponsePredicate, T> conditionsToResults,
			T defaultResult) {
		this.logger = Logger.getLogger(HttpServletRequestResponsePredicateMatcher.class);
		this.defaultResult = defaultResult;
		this.conditionsToResults = conditionsToResults;
		this.logger
				.debug(String.format("Initialized ServletRequestPredicateMatcher with %d branches and a default of %s",
						conditionsToResults.entrySet().size(),
						defaultResult == null ? "null" : defaultResult.getClass().getName()));
	}

	public HttpServletRequestResponsePredicateMatcher(T defaultFilter) {
		this(new HashMap(), defaultFilter);
	}

	public T match(HttpServletRequest request, HttpServletResponse response) {
		T result = this.defaultResult;

		for (Map.Entry<HttpServletRequestResponsePredicate, T> conditionAndFilter : this.conditionsToResults
				.entrySet()) {
			HttpServletRequestResponsePredicate condition = conditionAndFilter.getKey();
			T candidateResult = conditionAndFilter.getValue();
			this.logger.debug(String.format("Checking predicate for %s", candidateResult.getClass().getName()));
			if (condition.test(request, response)) {
				this.logger.debug(String.format("Predicate matched for %s", candidateResult.getClass().getName()));
				result = candidateResult;
				break;
			}
		}

		this.logger.debug(String.format("Returning %s", result == null ? "null" : result.getClass().getName()));
		return result;
	}

	public void setConditionsToResult(Map<HttpServletRequestResponsePredicate, T> conditionsToFilter) {
		this.conditionsToResults = conditionsToFilter;
	}

	public T getDefaultResult() {
		return this.defaultResult;
	}

	public void setDefaultResult(T defaultResult) {
		this.defaultResult = defaultResult;
	}

	public void setSingleConditionToResult(HttpServletRequestResponsePredicate condition, T filter) {
		this.conditionsToResults = Collections.singletonMap(condition, filter);
	}
}
