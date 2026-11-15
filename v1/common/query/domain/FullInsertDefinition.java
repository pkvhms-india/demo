package com.varicent.common.query.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FullInsertDefinition {
	private final String table;
	private final List<String> columns;
	private final QueryDefinition query;

	public FullInsertDefinition(String table, QueryDefinition query) {
		this.table = table;
		this.query = query;
		this.columns = new ArrayList();
	}

	public FullInsertDefinition(String table, String[] columns, QueryDefinition query) {
		this(table, query);
		this.columns.addAll(Arrays.asList(columns));
	}

	public List<String> getColumns() {
		return Collections.unmodifiableList(this.columns);
	}

	public QueryDefinition getQuery() {
		return this.query;
	}

	public String getTable() {
		return this.table;
	}
}
