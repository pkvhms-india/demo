package com.varicent.common.schema.dao;

import com.varicent.core.database.schema.TableDefinition;

public abstract class SchemaDaoBase implements SchemaDao {
	@Override
	public final TableDefinition getTable(String table) {
		return new TableDefinition(table, this.getColumns(table));
	}
}
