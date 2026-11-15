package com.varicent.common.schema.dao;

import java.util.List;

import com.varicent.core.database.schema.ColumnDefinition;
import com.varicent.core.database.schema.TableDefinition;

public interface SchemaDao {
	List<ColumnDefinition> getColumns(String var1);

	List<ColumnDefinition> getKeyColumns(String var1);

	TableDefinition getTable(String var1);
}
