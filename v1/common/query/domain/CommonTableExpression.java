package com.varicent.common.query.domain;

import java.util.List;

public class CommonTableExpression {
	private String expressionName;
	private List<String> columnNames;
	private QueryDefinition queryDefinition;

	public CommonTableExpression(String expressionName, List<String> columnNames, QueryDefinition queryDefinition) {
		this.expressionName = expressionName;
		this.columnNames = columnNames;
		this.queryDefinition = queryDefinition;
	}

	public CommonTableExpression(String expressionName, QueryDefinition queryDefinition) {
		this(expressionName, (List) null, queryDefinition);
	}

	public String getExpressionName() {
		return this.expressionName;
	}

	public void setExpressionName(String expressionName) {
		this.expressionName = expressionName;
	}

	public List<String> getColumnNames() {
		return this.columnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	public QueryDefinition getQueryDefinition() {
		return this.queryDefinition;
	}

	public void setQueryDefinition(QueryDefinition queryDefinition) {
		this.queryDefinition = queryDefinition;
	}
}
