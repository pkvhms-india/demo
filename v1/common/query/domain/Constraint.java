package com.varicent.common.query.domain;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import org.springframework.util.Assert;

import com.varicent.core.database.Database;

public class Constraint implements IConstraint {
	private final Object left;
	private Object right;
	private final Operator operator;

	public static Constraint newConstraint(String leftTable, String leftColumn, Operator operator, BigDecimal value) {
		DataField left = new DataField(leftTable, leftColumn);
		Literal right = new Literal(Literal.Type.DECIMAL, value);
		return new Constraint(left, operator, right);
	}

	public static Constraint newConstraint(String leftTable, String leftColumn, Operator operator, Integer value) {
		DataField left = new DataField(leftTable, leftColumn);
		Literal right = new Literal(Literal.Type.INTEGER, value);
		return new Constraint(left, operator, right);
	}

	public static Constraint newConstraint(String leftTable, String leftColumn, Operator operator, Variable value) {
		DataField left = new DataField(leftTable, leftColumn);
		return new Constraint(left, operator, value);
	}

	public static Constraint newConstraint(String leftTable, String leftColumn, Operator operator, Date value) {
		DataField left = new DataField(leftTable, leftColumn);
		Literal right = new Literal(Literal.Type.DATE, value);
		return new Constraint(left, operator, right);
	}

	public static Constraint newConstraint(String leftTable, String leftColumn, Operator operator, Literal literal) {
		DataField left = new DataField(leftTable, leftColumn);
		return new Constraint(left, operator, literal);
	}

	public static Constraint newConstraint(String leftTable, String leftColumn, Operator operator, Object value) {
		if (value instanceof Literal) {
			return newConstraint(leftTable, leftColumn, operator, (Literal) value);
		} else if (value instanceof DataField) {
			DataField field = (DataField) value;
			return newConstraint(leftTable, leftColumn, operator, field.getTable(), field.getColumn());
		} else if (value instanceof String) {
			return newConstraint(leftTable, leftColumn, operator, (String) value);
		} else if (value instanceof Integer) {
			return newConstraint(leftTable, leftColumn, operator, (Integer) value);
		} else if (value instanceof BigDecimal) {
			return newConstraint(leftTable, leftColumn, operator, (BigDecimal) value);
		} else if (value instanceof Date) {
			return newConstraint(leftTable, leftColumn, operator, (Date) value);
		} else if (value instanceof Timestamp) {
			return newConstraint(leftTable, leftColumn, operator, (Timestamp) value);
		} else if (value instanceof java.util.Date) {
			return newConstraint(leftTable, leftColumn, operator, new Timestamp(((java.util.Date) value).getTime()));
		} else if (value instanceof Variable) {
			return newConstraint(leftTable, leftColumn, operator, (Variable) value);
		} else {
			Assert.state(false);
			return null;
		}
	}

	public static Constraint newConstraint(String leftTable, String leftColumn, Operator operator, String value) {
		DataField left = new DataField(leftTable, leftColumn);
		Literal right = new Literal(Literal.Type.STRING,
				value.length() > 0 ? value : Database.getSyntax().getEmptyString());
		return new Constraint(left, operator, right);
	}

	public static Constraint newConstraint(String leftTable, String leftColumn, Operator operator, String rightTable,
			String rightColumn) {
		DataField left = new DataField(leftTable, leftColumn);
		DataField right = new DataField(rightTable, rightColumn);
		return new Constraint(left, operator, right);
	}

	public static Constraint newConstraint(String leftTable, String leftColumn, Operator operator, Timestamp value) {
		DataField left = new DataField(leftTable, leftColumn);
		Literal right = new Literal(Literal.Type.DATETIME, value);
		return new Constraint(left, operator, right);
	}

	public static Constraint newConstraint(String leftTable, String leftColumn, Operator operator, byte[] value) {
		DataField left = new DataField(leftTable, leftColumn);
		Literal right = new Literal(Literal.Type.BLOB, value);
		return new Constraint(left, operator, right);
	}

	public static Constraint newNullConstraint(String leftTable, String leftColumn, boolean isNull) {
		DataField left = new DataField(leftTable, leftColumn);
		return new Constraint(left, isNull);
	}

	private boolean IsUnaryOperator() {
		return this.operator == Constraint.Operator.IsNull || this.operator == Constraint.Operator.IsNotNull;
	}

	@Override
	public IConstraint Copy() {
		return this.IsUnaryOperator() ? new Constraint(this.left, this.operator)
				: new Constraint(this.left, this.operator, this.right);
	}

	private Constraint(Object left, boolean isNull) {
		this(left, isNull ? Constraint.Operator.IsNull : Constraint.Operator.IsNotNull);
	}

	private Constraint(Object left, Operator operator) {
		Assert.isTrue(this.isValidOperand(left));
		this.left = left;
		this.right = null;
		this.operator = operator;
	}

	private Constraint(Object left, Operator operator, Object right) {
		Assert.isTrue(this.isValidOperand(left));
		Assert.isTrue(this.isValidOperand(right));
		this.left = left;
		this.right = right;
		this.operator = operator;
	}

	public Object getLeft() {
		return this.left;
	}

	public void setRight(Object newRight) {
		this.right = newRight;
	}

	public Operator getOperator() {
		return this.operator;
	}

	public Object getRight() {
		return this.right;
	}

	private boolean isValidOperand(Object operand) {
		return operand instanceof Literal || operand instanceof DataField || operand instanceof Variable;
	}

	public static enum Operator {
		LessThan, LessThanEquals, Equals, NotEquals, GreaterThan, GreaterThanEquals, IsNull, IsNotNull;
	}
}
