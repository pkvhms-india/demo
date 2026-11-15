package com.varicent.common.schema.dao.cached;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.varicent.common.schema.dao.SchemaDao;
import com.varicent.common.schema.dao.SchemaDaoBase;
import com.varicent.core.database.schema.ColumnDefinition;

public class SchemaDaoCachedImpl extends SchemaDaoBase {
	private Map<String, List<ColumnDefinition>> columnsCache = new HashMap();
	private final SchemaDao schemaDao;

	public SchemaDaoCachedImpl(SchemaDao schemaDao) {
		this.schemaDao = schemaDao;
	}

	@Override
	public List<ColumnDefinition> getColumns(String table) {
		if (this.columnsCache.containsKey(table)) {
			return this.columnsCache.get(table);
		} else {
			List<ColumnDefinition> columnDefinitions = Collections.unmodifiableList(this.schemaDao.getColumns(table));
			this.columnsCache.put(table, columnDefinitions);
			return columnDefinitions;
		}
	}

	@Override
	public List<ColumnDefinition> getKeyColumns(String table) {
		List<ColumnDefinition> columnDefinitions = this.getColumns(table);
		List<ColumnDefinition> keyColumns = new ArrayList();

		for (ColumnDefinition columnDefinition : columnDefinitions) {
			if (columnDefinition.isKey()) {
				keyColumns.add(columnDefinition);
			}
		}

		return keyColumns;
	}
}
