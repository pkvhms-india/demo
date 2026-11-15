package com.varicent.common.schema.dao.cached;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.varicent.common.schema.dao.jdbc.JdbcSchemaDaoImpl;
import com.varicent.core.database.schema.ColumnDefinition;

public class JdbcSchemaDaoCachedImpl extends JdbcSchemaDaoImpl {
	private static Map<String, List<ColumnDefinition>> columnsCache = new HashMap();

	public JdbcSchemaDaoCachedImpl(DataSource ds) {
		super(ds);
	}

	@Override
	public List<ColumnDefinition> getColumns(String table) {
		if (columnsCache.containsKey(table)) {
			return columnsCache.get(table);
		} else {
			List<ColumnDefinition> columnDefinitions = Collections.unmodifiableList(super.getColumns(table));
			columnsCache.put(table, columnDefinitions);
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
