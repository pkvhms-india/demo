package com.varicent.common.time.domain;

public class RevisionLockPeriod {
	private final TimePeriod period;
	private int revision;

	public RevisionLockPeriod(TimePeriod period, int revision) {
		this.period = period;
		this.revision = revision;
	}

	public TimePeriod getPeriod() {
		return this.period;
	}

	public int getRevision() {
		return this.revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}
}
