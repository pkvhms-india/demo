package com.varicent.common.reference.dao;

import com.varicent.common.query.domain.DataField;
import com.varicent.common.reference.domain.Reference;
import com.varicent.common.schema.dao.SchemaDao;

public interface ReferenceDao {
	Reference getReference(String var1, String var2);

	DataField getSourceField(String var1);

	DataField getSourceFieldAt(String var1, int var2, SchemaDao var3);
}
