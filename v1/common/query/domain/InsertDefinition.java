package com.varicent.common.query.domain;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.util.StringUtils;

import com.varicent.common.query.sql.insert.InsertBuilder;
import com.varicent.core.database.Database;
import com.varicent.core.database.schema.ColumnDefinition;
import com.varicent.core.database.schema.Schema;
import com.varicent.core.exceptions.IllegalValueException;
import com.varicent.core.text.Format;

public class InsertDefinition {
	private final String table;
	private final List<String> columns;
	private final List<Literal> values;

	public InsertDefinition(String table) {
		this.table = table;
		this.columns = new ArrayList();
		this.values = new ArrayList();
	}

	public InsertDefinition(String table, List<String> columns) {
		this(table);
		this.columns.addAll(columns);
	}

	public InsertDefinition(String table, String[] columns) {
		this(table);
		this.columns.addAll(Arrays.asList(columns));
	}

	public void addColumn(String colname) {
		this.columns.add(colname);
	}

	public void addNullValue() {
		this.values.add(new Literal(Literal.Type.NULL, (Object) null));
	}

	public void addSystemDate() {
		this.values.add(new Literal(Literal.Type.FUNCTION, Function.getDate()));
	}

	public void addSystemDateTime() {
		this.values.add(new Literal(Literal.Type.FUNCTION, Function.getDateTime()));
	}

	public void addSystemUtcDate() {
		this.values.add(new Literal(Literal.Type.FUNCTION, Function.getDateUtc()));
	}

	public void addSystemUtcDateTime() {
		this.values.add(new Literal(Literal.Type.FUNCTION, Function.getDateTimeUtc()));
	}

	public void addValue(BigDecimal value) {
		this.addValue(Literal.Type.DECIMAL, value);
	}

	public void addValue(boolean value) {
		if (value) {
			this.addValue(1);
		} else {
			this.addValue(0);
		}

	}

	public void addValue(ColumnDefinition column, String value) throws IllegalValueException {
		this.addValue(column, value, true);
	}

	public void addValue(ColumnDefinition column, String value, boolean capitalizeStringKeys)
			throws IllegalValueException {
		int columnType = column.getType();
		if (value.length() == 0) {
			if (column.isKey()) {
				throw new IllegalValueException(column.getTable(), column.getDisplayName(), column.getName(), value);
			}

			if (column.isDateTime()) {
				if (!column.isNullable()) {
					throw new IllegalValueException(column.getTable(), column.getDisplayName(), column.getName(),
							value);
				}

				this.addNullValue();
			} else if (column.isVarchar()) {
				if (column.isNullable()) {
					this.addNullValue();
				} else {
					this.addValue("");
				}
			} else if (column.isNumeric()) {
				if (column.isNullable()) {
					this.addNullValue();
				} else {
					this.addValue(0);
				}
			} else if (column.isNullable()) {
				this.addNullValue();
			} else {
				Object literalValue = Schema.parse(value, columnType, false);
				if (literalValue == null) {
					throw new IllegalValueException(column.getTable(), column.getDisplayName(), column.getName(),
							value);
				}

				this.addValue(Schema.toLiteralType(columnType, literalValue), literalValue);
			}
		} else {
			if (!Schema.isValid(value, column.getType(), false)) {
				throw new IllegalValueException(column.getTable(), column.getDisplayName(), column.getName(), value);
			}

			Object literalValue = Schema.parse(value, columnType, false);
			if (literalValue == null) {
				throw new IllegalValueException(column.getTable(), column.getDisplayName(), column.getName(), value);
			}

			if (column.isVarchar() && column.isKey() && capitalizeStringKeys) {
				literalValue = ((String) literalValue).toUpperCase();
			}

			this.addValue(Schema.toLiteralType(columnType, literalValue), literalValue);
		}

	}

	public void addValue(int value) {
		this.addValue(Literal.Type.INTEGER, value);
	}

	public void addValue(Date value) {
		this.addValue(Literal.Type.DATE, value);
	}

	public void addValue(java.util.Date value) {
		this.addValue(Literal.Type.DATETIME, value);
	}

	public void addValue(byte[] value) {
		this.addValue(Literal.Type.BLOB, value);
	}

	public void addValue(Literal literal) {
		this.values.add(literal);
	}

	public void addValue(Literal.Type type, Object value) {
		this.values.add(new Literal(type, value));
	}

	public void addValue(String value) {
		this.addValue(Literal.Type.STRING,
				(Object) (value.length() > 0 ? value : Database.getSyntax().getEmptyString()));
	}

	public void addValue(Timestamp value) {
		this.addValue(Literal.Type.DATETIME, value);
	}

	public List<String> getColumns() {
		return Collections.unmodifiableList(this.columns);
	}

	public String getTable() {
		return this.table;
	}

	public List<Literal> getValues() {
		return Collections.unmodifiableList(this.values);
	}

	public String getValueString() {
		List<String> stringValues = new ArrayList();

		for (Literal value : this.values) {
			stringValues.add(Format.asValueString(value.getValue()));
		}

		return StringUtils.collectionToDelimitedString(stringValues, ", ");
	}

	@Override
	public String toString() {
		InsertBuilder ib = Database.getInsertBuilder();
		return ib.toSql(this);
	}
}
