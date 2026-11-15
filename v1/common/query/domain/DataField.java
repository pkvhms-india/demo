package com.varicent.common.query.domain;

public class DataField {
	private final String table;
	private final String column;

	public DataField(String table, String column) {
		this.table = table;
		this.column = column;
	}

	public DataField(DataField other) {
		this.table = other.table;
		this.column = other.column;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof DataField)) {
			return false;
		} else {
			DataField field = (DataField) o;
			if (!this.table.equals(field.table)) {
				return false;
			} else {
				return this.column.equals(field.column);
			}
		}
	}

	public String getColumn() {
		return this.column;
	}

	public String getTable() {
		return this.table;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + this.table.hashCode();
		result = 37 * result + this.column.hashCode();
		return result;
	}
}
