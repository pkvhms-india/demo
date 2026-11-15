package com.varicent.common.time.domain;

import java.util.Date;

public class TimePeriod {
	private final String timeId;
	private final String name;
	private final String level;
	private final String parent;
	private final Date starting;
	private final Date ending;

	public TimePeriod(String timeId, String name, String level, String parent, Date starting, Date ending) {
		this.timeId = timeId;
		this.name = name;
		this.level = level;
		this.parent = parent;
		this.starting = new Date(starting.getTime());
		this.ending = new Date(ending.getTime());
	}

	public Date getEnding() {
		return (Date) this.ending.clone();
	}

	public String getLevel() {
		return this.level;
	}

	public String getName() {
		return this.name;
	}

	public String getParent() {
		return this.parent;
	}

	public Date getStarting() {
		return (Date) this.starting.clone();
	}

	public String getTimeId() {
		return this.timeId;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
