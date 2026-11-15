package com.varicent.common.context.web.util;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthorityUtilExt {
	public static final List<GrantedAuthority> NO_AUTHORITIES = Collections.emptyList();

	public static boolean userHasAuthority(String authority) {
		for (GrantedAuthority grantedAuthority : getUserAuthorities()) {
			if (authority.equals(grantedAuthority.getAuthority())) {
				return true;
			}
		}

		return false;
	}

	private static Collection<? extends GrantedAuthority> getUserAuthorities() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth != null && auth.getAuthorities() != null ? auth.getAuthorities() : NO_AUTHORITIES;
	}
}
