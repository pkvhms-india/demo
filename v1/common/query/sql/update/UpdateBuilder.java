package com.varicent.common.query.sql.update;

import com.varicent.common.query.domain.UpdateDefinition;

public interface UpdateBuilder {
	String toSql(UpdateDefinition var1);
}
