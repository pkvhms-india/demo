package com.varicent.common.port.domain;

public class Port {
	public static final String PORT_PREFIX = "_Port";
	private final String name;
	private final String sourceTable;
	private final int id;
	private final PortColumn[] columns;

	public Port(int id, String name, String sourceTable, PortColumn[] columns) {
		this.id = id;
		this.name = name;
		this.sourceTable = sourceTable;
		this.columns = columns;
	}

	public PortColumn[] getColumns() {
		return this.columns;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getSourceTable() {
		return this.sourceTable;
	}

	public String getOutputTable() {
		return "_Port".concat(Integer.toString(this.id));
	}
}
