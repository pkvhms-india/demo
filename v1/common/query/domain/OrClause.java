package com.varicent.common.query.domain;

public class OrClause extends Clause {
	@Override
	public OrClause newClause() {
		return new OrClause();
	}

	@Override
	public OrClause newClause(IConstraint constraint) {
		OrClause clause = new OrClause();
		clause.addConstraint(constraint);
		return clause;
	}

	@Override
	public OrClause newClause(String table, String column, Constraint.Operator operator, Object value) {
		OrClause clause = new OrClause();
		clause.addConstraint(Constraint.newConstraint(table, column, operator, value));
		return clause;
	}

	@Override
	public OrClause newNullClause(String table, String column, boolean isNull) {
		OrClause clause = new OrClause();
		clause.addConstraint(Constraint.newNullConstraint(table, column, isNull));
		return clause;
	}

	@Override
	public Clause Copy() {
		OrClause clause = new OrClause();

		for (IConstraint constraint : this.constraints) {
			clause.addConstraint(constraint);
		}

		return clause;
	}
}
