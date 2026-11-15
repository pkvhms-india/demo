package com.varicent.common.query.sql.delete;

import com.varicent.common.query.domain.DeleteDefinition;

public interface DeleteBuilder {
	String toSql(DeleteDefinition var1);
}
