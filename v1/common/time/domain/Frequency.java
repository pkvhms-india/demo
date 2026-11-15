package com.varicent.common.time.domain;

public class Frequency {
	private final String timeId;
	private final String levelId;

	public Frequency(String timeId, String levelId) {
		this.timeId = timeId;
		this.levelId = levelId;
	}

	public String getLevelId() {
		return this.levelId;
	}

	public String getTimeId() {
		return this.timeId;
	}
}
