package com.varicent.common.query.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.util.StringUtils;

import com.varicent.common.query.sql.update.UpdateBuilder;
import com.varicent.core.database.Database;
import com.varicent.core.database.schema.ColumnDefinition;
import com.varicent.core.database.schema.Schema;
import com.varicent.core.exceptions.IllegalValueException;
import com.varicent.core.text.Format;

public class UpdateDefinition {
	private final String table;
	private final List<DataField> fields;
	private final List<UpdateSourceItem> sources;
	private final Clause restriction;
	private final List<SqlDataSource> crossJoins;

	public UpdateDefinition(String table) {
		this.table = table;
		this.fields = new ArrayList();
		this.sources = new ArrayList();
		this.restriction = new AndClause();
		this.crossJoins = new ArrayList();
	}

	public void crossJoin(QueryDefinition query, String alias) {
		this.crossJoins.add(new SqlDataSource(query, alias));
	}

	public void crossJoin(SqlDataSource source) {
		if (source.getSourceType().equals(SqlDataSource.Type.QUERY)) {
			this.crossJoin(source.getQuery(), source.getAlias());
		} else if (source.hasAlias()) {
			this.crossJoin(source.getTable(), source.getAlias());
		} else {
			this.crossJoin(source.getTable());
		}

	}

	public void crossJoin(String table) {
		this.crossJoins.add(new SqlDataSource(table));
	}

	public void crossJoin(String table, String alias) {
		this.crossJoins.add(new SqlDataSource(table, alias));
	}

	public Clause getRestriction() {
		return this.restriction;
	}

	public List<SqlDataSource> getCrossJoins() {
		return Collections.unmodifiableList(this.crossJoins);
	}

	public List<DataField> getFields() {
		return Collections.unmodifiableList(this.fields);
	}

	public List<UpdateSourceItem> getSources() {
		return Collections.unmodifiableList(this.sources);
	}

	public String getTable() {
		return this.table;
	}

	public String getValueString() {
		List<String> stringValues = new ArrayList();

		for (UpdateSourceItem source : this.sources) {
			stringValues.add(Format.asValueString(source.getLiteral().getValue()));
		}

		return StringUtils.collectionToDelimitedString(stringValues, ", ");
	}

	public void newConstraint(ColumnDefinition column, String value) throws IllegalValueException {
		String table = column.getTable();
		String columnName = column.getName();
		int columnType = column.getType();
		Constraint c;
		if (value.length() == 0) {
			if (column.isKey()) {
				throw new IllegalValueException(table, column.getDisplayName(), column.getName(), value);
			}

			if (column.isDateTime()) {
				if (!column.isNullable()) {
					throw new IllegalValueException(table, column.getDisplayName(), column.getName(), value);
				}

				c = Constraint.newNullConstraint(table, columnName, true);
			} else if (column.isVarchar()) {
				if (column.isNullable()) {
					c = Constraint.newNullConstraint(table, columnName, true);
				} else {
					c = Constraint.newConstraint(table, columnName, Constraint.Operator.Equals, "");
				}
			} else if (column.isNullable()) {
				c = Constraint.newNullConstraint(table, columnName, true);
			} else {
				Object literalValue = Schema.parse(value, columnType, false);
				if (literalValue == null) {
					throw new IllegalValueException(table, column.getDisplayName(), column.getName(), value);
				}

				c = Constraint.newConstraint(table, columnName, Constraint.Operator.Equals, literalValue);
			}
		} else {
			if (!Schema.isValid(value, columnType, false)) {
				throw new IllegalValueException(table, column.getDisplayName(), column.getName(), value);
			}

			Object literalValue = Schema.parse(value, columnType, false);
			if (literalValue == null) {
				throw new IllegalValueException(table, column.getDisplayName(), column.getName(), value);
			}

			c = Constraint.newConstraint(table, columnName, Constraint.Operator.Equals, literalValue);
		}

		this.restriction.addConstraint(c);
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, BigDecimal value) {
		this.restriction.newConstraint(leftTable, leftColumn, operator, value);
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, Date value) {
		this.restriction.newConstraint(leftTable, leftColumn, operator, value);
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, Integer value) {
		this.restriction.newConstraint(leftTable, leftColumn, operator, value);
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, Object value) {
		this.restriction.newConstraint(leftTable, leftColumn, operator, value);
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, String value) {
		this.restriction.newConstraint(leftTable, leftColumn, operator, value);
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, String rightTable,
			String rightColumn) {
		this.restriction.newConstraint(leftTable, leftColumn, operator, rightTable, rightColumn);
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, Timestamp value) {
		this.restriction.newConstraint(leftTable, leftColumn, operator, value);
	}

	public void newNullConstraint(String leftTable, String leftColumn, boolean isNull) {
		this.restriction.newNullConstraint(leftTable, leftColumn, isNull);
	}

	public void newCustomConstraint(CustomConstraint constraint) {
		this.restriction.addConstraint(constraint);
	}

	public void set(ColumnDefinition column, String value) throws IllegalValueException {
		String columnName = column.getName();
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

				this.setNull(columnName);
			} else if (column.isVarchar()) {
				if (column.isNullable()) {
					this.setNull(columnName);
				} else {
					this.set(columnName, "");
				}
			} else if (column.isNullable()) {
				this.setNull(columnName);
			} else {
				Object literalValue = Schema.parse(value, columnType, false);
				if (literalValue == null) {
					throw new IllegalValueException(column.getTable(), column.getDisplayName(), column.getName(),
							value);
				}

				this.set(columnName, Schema.toLiteralType(columnType, value), literalValue);
			}
		} else {
			if (!Schema.isValid(value, columnType, false)) {
				throw new IllegalValueException(column.getTable(), column.getDisplayName(), column.getName(), value);
			}

			Object literalValue = Schema.parse(value, columnType, false);
			if (literalValue == null) {
				throw new IllegalValueException(column.getTable(), column.getDisplayName(), column.getName(), value);
			}

			this.set(columnName, Schema.toLiteralType(columnType, literalValue), literalValue);
		}

	}

	public void set(String column, Date value) {
		this.fields.add(new DataField(this.table, column));
		this.sources.add(new UpdateSourceItem(Literal.newLiteral(value)));
	}

	public void set(String column, int value) {
		this.fields.add(new DataField(this.table, column));
		this.sources.add(new UpdateSourceItem(Literal.newLiteral(value)));
	}

	public void set(String column, Literal.Type type, Object value) {
		this.fields.add(new DataField(this.table, column));
		this.sources.add(new UpdateSourceItem(new Literal(type, value)));
	}

	public void set(String column, String value) {
		this.fields.add(new DataField(this.table, column));
		this.sources.add(new UpdateSourceItem(
				Literal.newLiteral(value.length() > 0 ? value : Database.getSyntax().getEmptyString())));
	}

	public void set(String column, String toTable, String toColumn) {
		this.fields.add(new DataField(this.table, column));
		this.sources.add(new UpdateSourceItem(new DataField(toTable, toColumn)));
	}

	public void set(String column, Timestamp value) {
		this.fields.add(new DataField(this.table, column));
		this.sources.add(new UpdateSourceItem(Literal.newLiteral(value)));
	}

	public void setNull(String column) {
		this.fields.add(new DataField(this.table, column));
		this.sources.add(new UpdateSourceItem(Literal.newNullLiteral()));
	}

	public void setSystemDate(String column) {
		this.fields.add(new DataField(this.table, column));
		this.sources.add(new UpdateSourceItem(Literal.newLiteral(Function.getDate())));
	}

	public void setSystemDateTime(String column) {
		this.fields.add(new DataField(this.table, column));
		this.sources.add(new UpdateSourceItem(Literal.newLiteral(Function.getDateTime())));
	}

	public String toSql() {
		UpdateBuilder ub = Database.getUpdateBuilder();
		return ub.toSql(this);
	}
}
