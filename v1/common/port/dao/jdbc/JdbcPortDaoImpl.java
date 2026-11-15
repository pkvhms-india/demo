package com.varicent.common.port.dao.jdbc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.varicent.common.history.dao.HistoryDao;
import com.varicent.common.port.dao.PortDao;
import com.varicent.common.port.domain.Port;
import com.varicent.common.port.domain.PortColumn;
import com.varicent.common.query.domain.Constraint;
import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.query.sql.QueryBuilder;
import com.varicent.common.schema.dao.SchemaDao;
import com.varicent.core.database.Database;

@Repository("portDao")
public class JdbcPortDaoImpl implements PortDao {
	private static final String TABLE_NAME = "PortDefinition";
	private static final String PORT_COLUMN_TABLE_NAME = "ComposerPortColumn";
	private final JdbcOperations operations;

	@Autowired
	public JdbcPortDaoImpl(DataSource dataSource) {
		this.operations = Database.getOperations(dataSource);
	}

	@Override
	public Map<String, Port> getPortMap() {
		QueryDefinition allPorts = this.getAllportsQuery();
		QueryBuilder queryBuilder = Database.getQueryBuilder();
		SqlRowSet result = this.operations.queryForRowSet(queryBuilder.toSql(allPorts));
		Map<Integer, List<PortColumn>> allPortColumns = this.getPortColumns();

		Map<String, Port> portMap;
		List<PortColumn> columns;
		for (portMap = new HashMap(); result.next(); this.addToPortMap(portMap, result, columns)) {
			int portID = result.getInt("PortID");
			if (allPortColumns.containsKey(portID)) {
				columns = allPortColumns.get(portID);
			} else {
				columns = new LinkedList();
			}
		}

		return portMap;
	}

	@Override
	public Map<String, Port> getPortMapAt(int revision, HistoryDao historyDao, SchemaDao schemaDao) {
		QueryDefinition allPorts = this.getAllportsQuery();
		if (revision != -1) {
			historyDao.historifyQuery(allPorts, revision, schemaDao);
		}

		QueryBuilder queryBuilder = Database.getQueryBuilder();
		SqlRowSet result = this.operations.queryForRowSet(queryBuilder.toSql(allPorts));
		Map<String, Port> portMap = new HashMap();

		List<PortColumn> columns;
		for (Map<Integer, List<PortColumn>> allPortColumns = this.getPortColumnsAt(revision, historyDao,
				schemaDao); result.next(); this.addToPortMap(portMap, result, columns)) {
			int portID = result.getInt("PortID");
			if (allPortColumns.containsKey(portID)) {
				columns = allPortColumns.get(portID);
			} else {
				columns = new LinkedList();
			}
		}

		return portMap;
	}

	private void addToPortMap(Map<String, Port> portMap, SqlRowSet result, List<PortColumn> columns) {
		portMap.put("_Port" + result.getInt("PortID"), new Port(result.getInt("PortID"), result.getString("Name"),
				result.getString("SourceTable"), (PortColumn[]) columns.toArray(new PortColumn[columns.size()])));
	}

	private Map<Integer, List<PortColumn>> getPortColumns() {
		QueryDefinition allColumns = this.getPortColumnsQuery();
		QueryBuilder queryBuilder = Database.getQueryBuilder();
		SqlRowSet result = this.operations.queryForRowSet(queryBuilder.toSql(allColumns));

		Map<Integer, List<PortColumn>> columnMap;
		Integer portID;
		for (columnMap = new HashMap(); result.next(); columnMap.get(portID).add(new PortColumn(result.getInt("Type"),
				result.getString("Name"), result.getString("SourceColumn"), result.getBoolean("IsKey")))) {
			portID = result.getInt("PortID");
			if (!columnMap.containsKey(portID)) {
				columnMap.put(portID, new LinkedList());
			}
		}

		return columnMap;
	}

	private Map<Integer, List<PortColumn>> getPortColumnsAt(int revision, HistoryDao historyDao, SchemaDao schemaDao) {
		QueryDefinition allColumns = this.getPortColumnsQuery();
		if (revision != -1) {
			historyDao.historifyQuery(allColumns, revision, schemaDao);
		}

		QueryBuilder queryBuilder = Database.getQueryBuilder();
		SqlRowSet result = this.operations.queryForRowSet(queryBuilder.toSql(allColumns));

		Map<Integer, List<PortColumn>> columnMap;
		Integer portID;
		for (columnMap = new HashMap(); result.next(); columnMap.get(portID).add(new PortColumn(result.getInt("Type"),
				result.getString("Name"), result.getString("SourceColumn"), result.getBoolean("IsKey")))) {
			portID = result.getInt("PortID");
			if (!columnMap.containsKey(portID)) {
				columnMap.put(portID, new LinkedList());
			}
		}

		return columnMap;
	}

	private List<PortColumn> getPortColumns(SqlRowSet columnResults) {
		List<PortColumn> columns = new LinkedList();

		while (columnResults.next()) {
			columns.add(new PortColumn(columnResults.getInt("Type"), columnResults.getString("Name"),
					columnResults.getString("SourceColumn"), columnResults.getBoolean("IsKey")));
		}

		return columns;
	}

	private QueryDefinition getAllportsQuery() {
		QueryDefinition allPorts = new QueryDefinition("PortDefinition");
		allPorts.select("PortDefinition", "PortID");
		allPorts.select("PortDefinition", "Name");
		allPorts.select("PortDefinition", "SourceTable");
		return allPorts;
	}

	private QueryDefinition getPortColumnsQuery() {
		QueryDefinition portColumns = new QueryDefinition("ComposerPortColumn");
		portColumns.selectAll();
		portColumns.orderBy("PortID");
		portColumns.orderBy("Order");
		return portColumns;
	}

	private QueryDefinition getPortColumnsQuery(int portId) {
		QueryDefinition portColumns = new QueryDefinition("ComposerPortColumn");
		portColumns.selectAll();
		portColumns.orderBy("Order");
		portColumns.newConstraint("ComposerPortColumn", "PortID", Constraint.Operator.Equals, portId);
		return portColumns;
	}

	private static class Columns {
		public static final String PORT_ID = "PortID";
		public static final String NAME = "Name";
		public static final String SOURCE_TABLE = "SourceTable";
		public static final String TYPE = "Type";
		public static final String ORDER = "Order";
		public static final String IS_KEY = "IsKey";
		public static final String SOURCE_COLUMN = "SourceColumn";
	}
}
