package com.varicent.common.query.domain;

public class GroupItem {
	private final String table;
	private final String column;

	public GroupItem(String table, String column) {
		this.table = table;
		this.column = column;
	}

	public GroupItem(GroupItem item) {
		this.table = item.table;
		this.column = item.column;
	}

	public String getTable() {
		return this.table;
	}

	public String getColumn() {
		return this.column;
	}

	public boolean hasTable() {
		return this.table != null;
	}
}
