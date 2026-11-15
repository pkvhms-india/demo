package com.varicent.common.query.domain;

public class OrderAggregate extends OrderItem {
	private final SelectAggregate.Type type;

	public OrderAggregate(String table, String column, OrderItem.Direction direction, SelectAggregate.Type type) {
		super(table, column, direction);
		this.type = type;
	}

	public SelectAggregate.Type getType() {
		return this.type;
	}
}
