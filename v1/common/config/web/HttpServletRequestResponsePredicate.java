package com.varicent.common.config.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HttpServletRequestResponsePredicate {
	boolean test(HttpServletRequest var1, HttpServletResponse var2);
}
