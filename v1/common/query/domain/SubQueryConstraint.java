package com.varicent.common.query.domain;

import org.springframework.util.Assert;

public class SubQueryConstraint implements IConstraint {
	private final DataField left;
	private final QueryDefinition right;
	private final Operator operator;

	public SubQueryConstraint(Operator operator, QueryDefinition right) {
		Assert.isTrue(
				operator == SubQueryConstraint.Operator.Exists || operator == SubQueryConstraint.Operator.NotExists);
		this.left = null;
		this.right = right;
		this.operator = operator;
	}

	public SubQueryConstraint(String table, String column, Operator operator, QueryDefinition right) {
		this.left = new DataField(table, column);
		this.right = right;
		this.operator = operator;
	}

	@Override
	public IConstraint Copy() {
		return new SubQueryConstraint(this.left.getTable(), this.left.getColumn(), this.operator, this.right);
	}

	public DataField getLeft() {
		return this.left;
	}

	public Operator getOperator() {
		return this.operator;
	}

	public QueryDefinition getRight() {
		return this.right;
	}

	public static enum Operator {
		In, NotIn, Exists, NotExists, Equals, LessThanEquals;
	}
}
