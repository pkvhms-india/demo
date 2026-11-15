package com.varicent.common.query.domain;

public class OrderItem {
	private final String table;
	private final String column;
	private final Direction direction;

	public OrderItem(String table, String column, Direction direction) {
		this.table = table;
		this.column = column;
		this.direction = direction;
	}

	public OrderItem(OrderItem item) {
		this.table = item.table;
		this.column = item.column;
		this.direction = item.direction;
	}

	public String getColumn() {
		return this.column;
	}

	public Direction getDirection() {
		return this.direction;
	}

	public String getTable() {
		return this.table;
	}

	public boolean hasTable() {
		return this.table != null;
	}

	public static enum Direction {
		ASCENDING, DESCENDING;
	}
}
