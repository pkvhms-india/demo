package com.varicent.common.plan.domain;

import com.varicent.core.text.Format;

public class Plan {
	private final String id;
	private final String name;
	private final String calendar;

	public Plan(Plan plan) {
		this(plan.getId(), plan.getName(), plan.getCalendar());
	}

	public Plan(String id, String name) {
		this(id, name, (String) null);
	}

	public Plan(String id, String name, String calendar) {
		this.id = id;
		this.name = name;
		this.calendar = calendar;
	}

	public String getCalendar() {
		return this.calendar;
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
