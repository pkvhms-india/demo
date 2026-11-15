package com.varicent.common.query.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeleteDefinition {
	private final String table;
	private final List<Join> joins;
	private final Clause restriction;

	public DeleteDefinition(String table) {
		this.table = table;
		this.joins = new ArrayList();
		this.restriction = new AndClause();
	}

	public List<Join> getJoins() {
		return this.joins;
	}

	public Clause getRestriction() {
		return this.restriction;
	}

	public String getTable() {
		return this.table;
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

	public void newCustomConstraint(CustomConstraint constraint) {
		this.restriction.addConstraint(constraint);
	}

	public void newNullConstraint(String leftTable, String leftColumn, boolean isNull) {
		this.restriction.newNullConstraint(leftTable, leftColumn, isNull);
	}
}
