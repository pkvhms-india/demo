package com.varicent.common.query.domain;

public class SelectLiteral implements ISelectItem {
	private final Literal value;
	private final String alias;

	public SelectLiteral(Literal value, String alias) {
		this.value = value;
		this.alias = alias;
	}

	@Override
	public ISelectItem Copy() {
		return new SelectLiteral(this.value, this.alias);
	}

	public String getAlias() {
		return this.alias;
	}

	public Literal getValue() {
		return this.value;
	}

	public boolean hasAlias() {
		return this.alias != null;
	}
}
