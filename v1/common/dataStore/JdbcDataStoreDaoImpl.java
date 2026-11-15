package com.varicent.common.dataStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;
import javax.xml.stream.XMLStreamException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.query.domain.SelectItem;
import com.varicent.common.query.domain.SourceDefinition;
import com.varicent.common.query.sql.QueryBuilder;
import com.varicent.common.query.xml.SourceDefinitionParser;
import com.varicent.core.database.Database;
import com.varicent.core.database.support.RowMapperUtil;

@Repository("dataStoreDao")
public class JdbcDataStoreDaoImpl implements DataStoreDao {
	private static final String TABLE_NAME = "DataStore";
	private static final String COLUMN_TABLE_NAME = "DataStoreColumn";
	private final JdbcOperations operations;

	@Autowired
	public JdbcDataStoreDaoImpl(DataSource dataSource) {
		this.operations = Database.getOperations(dataSource);
	}

	@Override
	public Map<Integer, SourceDefinition> getDataStoreMap() {
		QueryDefinition allDataStores = this.getAllDataStoresQuery();
		QueryBuilder queryBuilder = Database.getQueryBuilder();
		List<Map.Entry<Integer, String>> result = this.operations.query(queryBuilder.toSql(allDataStores),
				RowMapperUtil.getEntryMapper());
		Map<Integer, SourceDefinition> dsMap = new HashMap();
		Map<Integer, List<String>> columns = this.GetColumnDictionary();

		for (Entry<Integer, String> item : result) {
			int dsID = item.getKey();

			try {
				SourceDefinition source = SourceDefinitionParser.getSourceDefinition(item.getValue());
				List<String> cols = columns.get(dsID);

				for (int i = 0; i < cols.size(); ++i) {
					String newAlias = cols.get(i);
					SelectItem sItem = (SelectItem) source.getQuery().getSelect().get(i);
					sItem.setAlias(newAlias);
				}

				dsMap.put(dsID, source);
			} catch (InvalidResultSetAccessException e) {
				e.printStackTrace();
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}

		return dsMap;
	}

	private Map<Integer, List<String>> GetColumnDictionary() {
		Map<Integer, List<String>> columns = new HashMap();
		QueryDefinition qd = new QueryDefinition("DataStoreColumn");
		qd.select("DataStoreColumn", "DataStoreID");
		qd.select("DataStoreColumn", "ColumnName");
		qd.orderBy("Order");
		QueryBuilder queryBuilder = Database.getQueryBuilder();

		int id;
		String name;
		for (SqlRowSet result = this.operations.queryForRowSet(queryBuilder.toSql(qd)); result.next(); columns.get(id)
				.add(name)) {
			id = result.getInt("DataStoreID");
			name = result.getString("ColumnName");
			if (!columns.containsKey(id)) {
				columns.put(id, new ArrayList());
			}
		}

		return columns;
	}

	private QueryDefinition getAllDataStoresQuery() {
		QueryDefinition allDS = new QueryDefinition("DataStore");
		allDS.select("DataStore", "DataStoreID");
		allDS.select("DataStore", "Definition");
		return allDS;
	}

	private static class Columns {
		public static final String DATA_STORE_ID = "DataStoreID";
		public static final String DEFINITION = "Definition";
	}

	private static class ColumnTableColumns {
		public static final String DATA_STORE_ID = "DataStoreID";
		public static final String COLUMN_NAME = "ColumnName";
		public static final String ORDER = "Order";
	}
}
