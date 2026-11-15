package com.varicent.common.query.domain;

public class SelectItem implements ISelectItem {
	private final String table;
	private final String column;
	private String alias;
	private final Type type;

	public SelectItem(String table, String column) {
		this(table, column, (String) null, SelectItem.Type.NONE);
	}

	public SelectItem(String table, String column, String alias, Type type) {
		this.table = table;
		this.column = column;
		this.alias = alias;
		this.type = type;
	}

	@Override
	public ISelectItem Copy() {
		return new SelectItem(this.table, this.column, this.alias, this.type);
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

	public void setAlias(String newAlias) {
		this.alias = newAlias;
	}

	public static enum Type {
		INT, DECIMAL, DATE_TIME, STRING, BLOB, NONE;
	}
}
