package com.varicent.common.query.domain;

public class SelectAll implements ISelectItem {
	@Override
	public ISelectItem Copy() {
		return new SelectAll();
	}
}
