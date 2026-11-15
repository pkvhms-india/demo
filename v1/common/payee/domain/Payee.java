package com.varicent.common.payee.domain;

import java.io.Serializable;

import org.springframework.util.StringUtils;

import com.varicent.core.text.Format;

public class Payee implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String id;
	private final String name;
	private String email;

	public Payee(Payee payee) {
		this(payee.getId(), payee.getName(), payee.getEmail());
	}

	public Payee(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public Payee(String id, String name, String email) {
		this(id, name);
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public boolean hasEmail() {
		return StringUtils.hasText(this.email);
	}

	public String getDisplayName() {
		return this.toString();
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String GetFormattedName() {
		return this.getName() != null && this.getName().trim().length() != 0
				? String.format("%s (%s)", this.getName(), this.getId())
				: this.getId();
	}

	@Override
	public String toString() {
		return Format.asObjectString(this.id, this.name);
	}
}
