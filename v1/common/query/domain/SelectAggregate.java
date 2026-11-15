package com.varicent.common.query.domain;

import org.springframework.util.Assert;

public class SelectAggregate implements ISelectItem {
	private final Type type;
	private final String table;
	private final String column;
	private final String alias;

	public SelectAggregate(Type type) {
		this(type, (String) null, (String) null, (String) null);
		Assert.isTrue(type == SelectAggregate.Type.COUNT);
	}

	public SelectAggregate(Type type, String alias) {
		this(type, (String) null, (String) null, alias);
		Assert.isTrue(type == SelectAggregate.Type.COUNT);
	}

	public SelectAggregate(Type type, String table, String column, String alias) {
		this.type = type;
		this.table = table;
		this.column = column;
		this.alias = alias;
	}

	public SelectAggregate(Type type, SelectItem item) {
		this.type = type;
		this.table = item.getTable();
		this.column = item.getColumn();
		this.alias = item.getAlias();
	}

	@Override
	public ISelectItem Copy() {
		return new SelectAggregate(this.type, this.table, this.column, this.alias);
	}

	public String getAlias() {
		return this.alias;
	}

	public String getColumn() {
		return this.column;
	}

	public String getTable() {
		return this.table;
	}

	public Type getType() {
		return this.type;
	}

	public boolean hasAlias() {
		return this.alias != null;
	}

	public static enum Type {
		SUM, AVG, COUNT, MAX, MIN;
	}
}
