package com.varicent.common.reference.domain;

public class Reference {
	private final String table;
	private final String column;
	private final String referencedTable;
	private final String referencedColumn;

	public Reference(String table, String column, String referencedTable, String referencedColumn) {
		this.table = table;
		this.column = column;
		this.referencedTable = referencedTable;
		this.referencedColumn = referencedColumn;
	}

	public String getColumn() {
		return this.column;
	}

	public String getReferencedColumn() {
		return this.referencedColumn;
	}

	public String getReferencedTable() {
		return this.referencedTable;
	}

	public String getTable() {
		return this.table;
	}
}
