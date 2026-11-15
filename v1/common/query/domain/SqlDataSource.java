package com.varicent.common.query.domain;

public class SqlDataSource {
	private final String table;
	private final QueryDefinition query;
	private final String alias;
	private final Type sourceType;

	public SqlDataSource(QueryDefinition query) {
		this(query, (String) null);
	}

	public SqlDataSource(SqlDataSource source) {
		this.sourceType = source.sourceType;
		if (source.sourceType == SqlDataSource.Type.TABLE) {
			this.table = source.table;
			this.query = null;
		} else {
			this.table = null;
			this.query = new QueryDefinition(source.query);
		}

		if (source.hasAlias()) {
			this.alias = source.alias;
		} else {
			this.alias = null;
		}

	}

	public SqlDataSource(QueryDefinition query, String alias) {
		this.table = null;
		this.query = query;
		this.alias = alias;
		this.sourceType = SqlDataSource.Type.QUERY;
	}

	public SqlDataSource(String table) {
		this(table, (String) null);
	}

	public SqlDataSource(String table, String alias) {
		this.table = table;
		this.query = null;
		this.alias = alias;
		this.sourceType = SqlDataSource.Type.TABLE;
	}

	public String getAlias() {
		return this.alias;
	}

	public String getName() {
		return this.hasAlias() ? this.alias : this.table;
	}

	public QueryDefinition getQuery() {
		return this.query;
	}

	public Type getSourceType() {
		return this.sourceType;
	}

	public String getTable() {
		return this.table;
	}

	public boolean hasAlias() {
		return this.alias != null;
	}

	public boolean isQuery() {
		return this.sourceType.equals(SqlDataSource.Type.QUERY);
	}

	public boolean isTable() {
		return this.sourceType.equals(SqlDataSource.Type.TABLE);
	}

	public static enum Type {
		TABLE, QUERY;
	}
}
