package com.varicent.common.query.domain;

public class Join {
	private SqlDataSource source;
	private final Type joinType;
	private Clause restriction;

	public Join(QueryDefinition query, String alias, Type joinType) {
		this(new SqlDataSource(query, alias), joinType);
	}

	public Join(Join join) {
		this(new SqlDataSource(join.source), join.joinType);
		this.restriction = (Clause) join.restriction.Copy();
	}

	public Join(SqlDataSource source, Type joinType) {
		this.source = source;
		this.joinType = joinType;
		this.restriction = new AndClause();
	}

	public Join(String table, String alias, Type joinType) {
		this(new SqlDataSource(table, alias), joinType);
	}

	public Join(String table, Type joinType) {
		this(new SqlDataSource(table), joinType);
	}

	public void addClause(Clause clause) {
		this.restriction.addConstraint(clause);
	}

	public Type getJoinType() {
		return this.joinType;
	}

	public Clause getRestriction() {
		return this.restriction;
	}

	public SqlDataSource getSource() {
		return this.source;
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, String rightTable,
			String rightColumn) {
		this.restriction.newConstraint(leftTable, leftColumn, operator, rightTable, rightColumn);
	}

	public void addConstraint(IConstraint constraint) {
		this.restriction.addConstraint(constraint);
	}

	public void setRestriction(Clause restriction) {
		if (restriction == null) {
			this.restriction = new AndClause();
		} else {
			this.restriction = restriction;
		}

	}

	public void setSource(SqlDataSource source) {
		this.source = source;
	}

	public static enum Type {
		INNER, LEFT, RIGHT, CROSS;
	}
}
