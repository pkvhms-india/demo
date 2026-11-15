package com.varicent.common.config.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class ConditionalFilter implements Filter {
	Logger logger;
	HttpServletRequestResponsePredicateMatcher<Filter> predicateToFilterMatcher;

	public ConditionalFilter(Map<HttpServletRequestResponsePredicate, Filter> conditionsToFilter,
			Filter defaultFilter) {
		this.logger = Logger.getLogger(ConditionalFilter.class);
		this.predicateToFilterMatcher = new HttpServletRequestResponsePredicateMatcher<>(conditionsToFilter,
				defaultFilter);
		this.logger.debug("Initialized ConditionalFilter");
	}

	public ConditionalFilter(Map<HttpServletRequestResponsePredicate, Filter> conditionsToFilter) {
		this(conditionsToFilter, (Filter) null);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		Filter filter = this.predicateToFilterMatcher.match((HttpServletRequest) request,
				(HttpServletResponse) response);
		if (filter == null) {
			this.logger.debug("No match for predicate. Default filter has not been set. Resuming filter chain.");
			filterChain.doFilter(request, response);
		} else {
			this.logger.debug(String.format("Invoking filter %s", filter.getClass().getName()));
			filter.doFilter(request, response, filterChain);
		}

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void setConditionsToFilters(Map<HttpServletRequestResponsePredicate, Filter> conditionsToFilters) {
		this.predicateToFilterMatcher.setConditionsToResult(conditionsToFilters);
	}

	public void setSingleConditionToFilter(HttpServletRequestResponsePredicate condition, Filter filter) {
		this.predicateToFilterMatcher.setSingleConditionToResult(condition, filter);
	}

	@Override
	public void destroy() {
	}
}
