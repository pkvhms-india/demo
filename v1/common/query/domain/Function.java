package com.varicent.common.query.domain;

public class Function {
	private final Type type;

	public static Function getDate() {
		return new Function(Function.Type.GET_DATE);
	}

	public static Function getDateUtc() {
		return new Function(Function.Type.GET_DATE_UTC);
	}

	public static Function getDateTime() {
		return new Function(Function.Type.GET_DATETIME);
	}

	public static Function getDateTimeUtc() {
		return new Function(Function.Type.GET_DATETIME_UTC);
	}

	private Function(Type type) {
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}

	public static enum Type {
		GET_DATE, GET_DATETIME, GET_DATE_UTC, GET_DATETIME_UTC;
	}
}
