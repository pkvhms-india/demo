package com.varicent.common.query.domain;

import java.util.HashMap;
import java.util.Map;

import com.varicent.security.domain.UserAccount;

public class SourceDefinition {
	private static final String EXPLICIT_JOIN_PREFIX = "_E";
	public static final String IMPLICIT_JOIN_PREFIX = "_I";
	public static final String COLUMN_ALIAS_PREFIX = "_Column";
	private QueryDefinition query;
	private final Map<String, String> sourceNames;

	public SourceDefinition(QueryDefinition query) {
		this.query = query;
		this.sourceNames = new HashMap();
	}

	public String toDisplaySourceName(String table) {
		return this.sourceNames.containsKey(table) ? (String) this.sourceNames.get(table) : table;
	}

	public void addSourceName(String alias, String name) {
		this.sourceNames.put(alias, name);
	}

	public void constrainToWebUser() {
		if (this.sourceNames.containsValue("WebUser_")) {
			String joinAlias = this.resolveAlias("WebUser_");
			QueryDefinition queryWebUser = new QueryDefinition(this.query);
			queryWebUser.clearJoins();

			for (Join join : this.query.getJoins()) {
				SqlDataSource source = join.getSource();
				if (source.hasAlias() && source.getAlias().equals(joinAlias) && source.isQuery()) {
					QueryDefinition qd = source.getQuery();
					if (qd.getSource().isTable() && qd.getSource().getTable().equals("Payee_")) {
						qd.addConstraint(Constraint.newConstraint(joinAlias, "PayeeID_", Constraint.Operator.Equals,
								UserAccount.getCurrent().getPayeeId()));
					}
				}

				queryWebUser.joinWith(join);
			}

			this.query = queryWebUser;
		}

	}

	public String getMainSource() {
		return this.query.getSource().getAlias();
	}

	public QueryDefinition getQuery() {
		return this.query;
	}

	public void setQuery(QueryDefinition query) {
		this.query = query;
	}

	public String resolveAlias(String sourceName) {
		if (this.sourceNames.containsValue(sourceName)) {
			String result = this.reverseLookupAlias(sourceName);
			if (result != null) {
				return result;
			}
		}

		if (sourceName.equals("WebUser_")) {
			String result = this.reverseLookupAlias("WebUser_");
			if (result != null) {
				return result;
			}
		}

		return sourceName;
	}

	private String reverseLookupAlias(String lookupKey) {
		for (Map.Entry<String, String> entry : this.sourceNames.entrySet()) {
			if (entry.getValue().equals(lookupKey)) {
				return entry.getKey();
			}
		}

		return null;
	}

	public String resolveSource(String alias) {
		return this.sourceNames.containsKey(alias) ? (String) this.sourceNames.get(alias) : alias;
	}

	public String resolveTable(String sourceName) {
		String table = this.resolveAlias(sourceName);
		if (table.startsWith("_E")) {
			for (Join join : this.query.getJoins()) {
				SqlDataSource source = join.getSource();
				if (source.getAlias().equals(table)) {
					return this.getActualTableName(source);
				}
			}
		}

		return table;
	}

	private String getActualTableName(SqlDataSource source) {
		return source.isTable() ? source.getTable() : this.getActualTableName(source.getQuery().getSource());
	}
}
