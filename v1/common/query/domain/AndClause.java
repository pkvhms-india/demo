package com.varicent.common.query.domain;

public class AndClause extends Clause {
	@Override
	public AndClause newClause() {
		return new AndClause();
	}

	@Override
	public AndClause newClause(IConstraint constraint) {
		AndClause clause = new AndClause();
		clause.addConstraint(constraint);
		return clause;
	}

	@Override
	public AndClause newClause(String table, String column, Constraint.Operator operator, Object value) {
		AndClause clause = new AndClause();
		clause.addConstraint(Constraint.newConstraint(table, column, operator, value));
		return clause;
	}

	@Override
	public AndClause newNullClause(String table, String column, boolean isNull) {
		AndClause clause = new AndClause();
		clause.addConstraint(Constraint.newNullConstraint(table, column, isNull));
		return clause;
	}

	@Override
	public Clause Copy() {
		AndClause clause = new AndClause();

		for (IConstraint constraint : this.constraints) {
			clause.addConstraint(constraint.Copy());
		}

		return clause;
	}
}
