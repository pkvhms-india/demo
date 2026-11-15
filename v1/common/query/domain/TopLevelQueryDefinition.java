package com.varicent.common.query.domain;

import java.util.List;

public class TopLevelQueryDefinition {
	private QueryDefinition query;
	private List<CommonTableExpression> cteList;
	private Variable variable;

	public TopLevelQueryDefinition(QueryDefinition query, List<CommonTableExpression> cte, Variable variable) {
		this.query = query;
		this.cteList = cte;
		this.variable = variable;
	}

	public void addCommonTableExpression(CommonTableExpression cte) {
		this.cteList.add(cte);
	}

	public Variable getVariable() {
		return this.variable;
	}

	public QueryDefinition getQuery() {
		return this.query;
	}

	public List<CommonTableExpression> getCommonTableExpressions() {
		return this.cteList;
	}

	public void setQuery(QueryDefinition query) {
		this.query = query;
	}
}
