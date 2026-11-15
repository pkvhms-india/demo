package com.varicent.common.port.domain;

public class PortColumn {
	private int type;
	private String name;
	private String sourceColumn;
	private boolean isKey;

	public PortColumn(int type, String name, String sourceColumn, boolean isKey) {
		this.type = type;
		this.name = name;
		this.sourceColumn = sourceColumn;
		this.isKey = isKey;
	}

	public String getName() {
		return this.name;
	}

	public String getSourceColumn() {
		return this.sourceColumn;
	}

	public int getType() {
		return this.type;
	}

	public boolean isKey() {
		return this.isKey;
	}

	public int getJavaSqlType() {
		switch (this.type) {
		case 0:
		case 4:
			return 12;
		case 1:
			return 91;
		case 2:
			return 3;
		case 3:
		default:
			return 4;
		}
	}
}
