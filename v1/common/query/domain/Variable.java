package com.varicent.common.query.domain;

public class Variable {
	private String name;
	private SelectItem.Type type;
	private Object initialValue;

	public Variable(String name, SelectItem.Type type, Object initialValue) {
		this.name = name;
		this.type = type;
		this.initialValue = initialValue;
	}

	public String getName() {
		return this.name;
	}

	public SelectItem.Type getType() {
		return this.type;
	}

	public Object getInitialValue() {
		return this.initialValue;
	}
}
