package com.varicent.common.query.domain;

import org.springframework.util.Assert;

public class CustomConstraint implements IConstraint {
	private String sqlServerConstraint;

	public CustomConstraint(String sqlServerConstraint) {
		Assert.isTrue(sqlServerConstraint != null);
		this.sqlServerConstraint = sqlServerConstraint;
	}

	public String getSqlServerConstraint() {
		return this.sqlServerConstraint;
	}

	@Override
	public IConstraint Copy() {
		return new CustomConstraint(this.sqlServerConstraint);
	}
}
