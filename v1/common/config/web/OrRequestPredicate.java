package com.varicent.common.config.web;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class OrRequestPredicate implements HttpServletRequestResponsePredicate {
	Set<HttpServletRequestResponsePredicate> subPredicates;
	Logger logger = Logger.getLogger(OrRequestPredicate.class);

	public OrRequestPredicate(Collection<HttpServletRequestResponsePredicate> subPredicates) {
		this.subPredicates = new HashSet(subPredicates);
	}

	@Override
	public boolean test(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Processing 'OR' clause");

		for (HttpServletRequestResponsePredicate p : this.subPredicates) {
			this.logger.trace(String.format("Matching against %s", p.getClass().getName()));
			if (p.test(request, response)) {
				this.logger.debug(String.format("Match found: %s", p.getClass().getName()));
				return true;
			}
		}

		return false;
	}
}
