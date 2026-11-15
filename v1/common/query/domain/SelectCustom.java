package com.varicent.common.query.domain;

public class SelectCustom implements ISelectItem {
	private String sqlServerSelect;
	public final String Alias;

	public SelectCustom(String sqlServer, String alias) {
		this.sqlServerSelect = sqlServer;
		this.Alias = alias;
	}

	@Override
	public ISelectItem Copy() {
		return new SelectCustom(this.sqlServerSelect, this.Alias);
	}

	public boolean hasAlias() {
		return this.Alias != null;
	}

	public String getSqlServer() {
		return this.sqlServerSelect;
	}

	public String getAlias() {
		return this.Alias;
	}
}
