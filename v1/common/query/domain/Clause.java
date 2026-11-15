package com.varicent.common.query.domain;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Clause implements IConstraint {
	protected final List<IConstraint> constraints = new ArrayList();

	public abstract Clause newClause();

	public abstract Clause newClause(IConstraint var1);

	public abstract Clause newClause(String var1, String var2, Constraint.Operator var3, Object var4);

	public abstract Clause newNullClause(String var1, String var2, boolean var3);

	@Override
	public abstract IConstraint Copy();

	public void addConstraint(IConstraint constraint) {
		this.constraints.add(constraint);
	}

	public List<IConstraint> getConstraints() {
		return Collections.unmodifiableList(this.constraints);
	}

	public boolean hasConstraints() {
		return !this.constraints.isEmpty();
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, BigDecimal value) {
		this.constraints.add(Constraint.newConstraint(leftTable, leftColumn, operator, value));
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, Date value) {
		this.constraints.add(Constraint.newConstraint(leftTable, leftColumn, operator, value));
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, Integer value) {
		this.constraints.add(Constraint.newConstraint(leftTable, leftColumn, operator, value));
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, Long value) {
		this.constraints.add(Constraint.newConstraint(leftTable, leftColumn, operator, value));
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, Variable value) {
		this.constraints.add(Constraint.newConstraint(leftTable, leftColumn, operator, value));
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, Object value) {
		this.constraints.add(Constraint.newConstraint(leftTable, leftColumn, operator, value));
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, String value) {
		this.constraints.add(Constraint.newConstraint(leftTable, leftColumn, operator, value));
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, String rightTable,
			String rightColumn) {
		this.constraints.add(Constraint.newConstraint(leftTable, leftColumn, operator, rightTable, rightColumn));
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, Timestamp value) {
		this.constraints.add(Constraint.newConstraint(leftTable, leftColumn, operator, value));
	}

	public void newNullConstraint(String leftTable, String leftColumn, boolean isNull) {
		this.constraints.add(Constraint.newNullConstraint(leftTable, leftColumn, isNull));
	}

	public static enum Type {
		AND, OR;
	}
}
