package com.varicent.common.config.web;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

public class SavedRequestFullURLRegexMatchingPredicate implements HttpServletRequestResponsePredicate {
	private HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
	private Pattern pattern;
	private static Logger logger = LoggerFactory.getLogger(SavedRequestFullURLRegexMatchingPredicate.class);

	public SavedRequestFullURLRegexMatchingPredicate(String patternString) {
		this.pattern = Pattern.compile(patternString);
	}

	@Override
	public boolean test(HttpServletRequest request, HttpServletResponse response) {
		SavedRequest savedRequest = this.requestCache.getRequest(request, response);
		String fullSavedURL = savedRequest.getRedirectUrl();
		boolean matched = this.pattern.matcher(fullSavedURL).matches();
		if (logger.isDebugEnabled()) {
			logger.debug("Testing saved request '{}' against regex '{}': '{}'",
					new Object[] { fullSavedURL, this.pattern.pattern(), matched ? "MATCHED" : "did not match" });
		}

		return matched;
	}
}
