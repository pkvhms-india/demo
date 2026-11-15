package com.varicent.common.query.domain;

public class SelectTableAll implements ISelectItem {
	private final String table;

	public SelectTableAll(String table) {
		this.table = table;
	}

	@Override
	public ISelectItem Copy() {
		return new SelectTableAll(this.table);
	}

	public String getTable() {
		return this.table;
	}
}
