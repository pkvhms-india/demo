package com.varicent.common.query.domain;

public class UpdateSourceItem {
	private final DataField field;
	private final Literal literal;
	private final SourceType type;

	public UpdateSourceItem(DataField field) {
		this.field = field;
		this.literal = null;
		this.type = UpdateSourceItem.SourceType.FIELD;
	}

	public UpdateSourceItem(Literal literal) {
		this.field = null;
		this.literal = literal;
		this.type = UpdateSourceItem.SourceType.VALUE;
	}

	public DataField getField() {
		return this.field;
	}

	public Literal getLiteral() {
		return this.literal;
	}

	public SourceType getType() {
		return this.type;
	}

	public static enum SourceType {
		VALUE, FIELD;
	}
}
