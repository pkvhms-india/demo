package com.varicent.common.query.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class Literal {
	private final Type type;
	private final Object value;

	public static Literal newLiteral(BigDecimal value) {
		return new Literal(Literal.Type.DECIMAL, value);
	}

	public static Literal newLiteral(Date value) {
		return new Literal(Literal.Type.DATE, value);
	}

	public static Literal newLiteral(Function value) {
		return new Literal(Literal.Type.FUNCTION, value);
	}

	public static Literal newLiteral(int value) {
		return new Literal(Literal.Type.INTEGER, value);
	}

	public static Literal newLiteral(String value) {
		int length = value == null ? 0 : value.length();
		return new Literal(length > 100 ? Literal.Type.LONG_STRING : Literal.Type.STRING, value);
	}

	public static Literal newLiteral(Timestamp value) {
		return new Literal(Literal.Type.DATETIME, value);
	}

	public static Literal newNullLiteral() {
		return new Literal(Literal.Type.NULL, (Object) null);
	}

	public Literal(Type type, Object value) {
		this.type = type;
		this.value = value;
	}

	public Type getType() {
		return this.type;
	}

	public Object getValue() {
		return this.value;
	}

	public static enum Type {
		NULL, INTEGER, DECIMAL, DATE, DATETIME, STRING, LONG_STRING, FUNCTION, SYSTEMTIME, BLOB;
	}
}
