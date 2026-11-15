package com.varicent.common.calculation.domain;

import java.util.Date;

public class Calculation {
	public static final int MATHEMATICAL_TYPE = 0;
	public static final int SORT_TYPE = 1;
	public static final int TIMESHIFT_TYPE = 2;
	public static final int CATEGORY_TYPE = 4;
	private final int id;
	private final int calculationType;
	private final String name;
	private final String comment;
	private String timeLockCalendar;
	private String level;
	private final String resetLevel;
	private Date start;
	private Date end;

	public Calculation(int id, int type, String name, String comment, String timeId, String timeLevel,
			String resetLevel, Date start, Date end) {
		this.id = id;
		this.calculationType = type;
		this.name = name;
		this.comment = comment;
		this.timeLockCalendar = timeId;
		this.level = timeLevel;
		this.resetLevel = resetLevel;
		this.start = start == null ? null : new Date(start.getTime());
		this.end = end == null ? null : new Date(end.getTime());
	}

	public String getComment() {
		return this.comment;
	}

	public Date getEnd() {
		return this.end == null ? null : (Date) this.end.clone();
	}

	public int getId() {
		return this.id;
	}

	public String getLevel() {
		return this.level;
	}

	public String getName() {
		return this.name;
	}

	public String getResetLevel() {
		return this.resetLevel;
	}

	public Date getStart() {
		return this.start == null ? null : (Date) this.start.clone();
	}

	public String getTimeLockCalendar() {
		return this.timeLockCalendar;
	}

	public int getType() {
		return this.calculationType;
	}

	public boolean hasTimeLock() {
		return this.timeLockCalendar != null && this.timeLockCalendar.length() > 0;
	}

	public void setTimeLockDetails(String calendar, String level, Date start, Date end) {
		this.timeLockCalendar = calendar;
		this.level = level;
		this.start = start == null ? null : new Date(start.getTime());
		this.end = end == null ? null : new Date(end.getTime());
	}
}
