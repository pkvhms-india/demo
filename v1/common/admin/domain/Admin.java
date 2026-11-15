package com.varicent.common.admin.domain;

import com.varicent.core.text.Format;

public class Admin {
	private final String id;
	private final String name;
	private final String email;

	public Admin(String id, String name, String email) {
		this.id = id;
		this.name = name;
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

	@Override
	public String toString() {
		return Format.asObjectString(this.id, this.name);
	}
}
