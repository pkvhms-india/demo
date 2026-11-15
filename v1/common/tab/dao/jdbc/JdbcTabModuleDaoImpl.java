package com.varicent.common.tab.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.varicent.common.query.domain.Constraint;
import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.query.sql.QueryBuilder;
import com.varicent.common.tab.dao.TabModuleDao;
import com.varicent.common.tab.domain.TabModule;
import com.varicent.core.database.Database;
import com.varicent.core.database.support.RowMapperUtil;

@Repository("tabModuleDao")
public class JdbcTabModuleDaoImpl implements TabModuleDao {
	private static final String TABLE_NAME = "WebModule";
	private final JdbcOperations operations;

	@Autowired
	public JdbcTabModuleDaoImpl(DataSource dataSource) {
		this.operations = Database.getOperations(dataSource);
	}

	@Override
	public TabModule.ModuleType getModuleType(int workflowObjectId) {
		QueryDefinition moduleTypeQuery = new QueryDefinition("WebModule");
		moduleTypeQuery.select("WebModule", "ModuleType");
		moduleTypeQuery.newConstraint("WebModule", "WorkflowObjectID", Constraint.Operator.Equals, workflowObjectId);
		QueryBuilder queryBuilder = Database.getQueryBuilder();
		List<Integer> result = this.operations.query(queryBuilder.toSql(moduleTypeQuery),
				RowMapperUtil.getIntegerMapper());
		return result.isEmpty() ? null : TabModule.ModuleType.getType(result.get(0));
	}

	@Override
	public Map<Integer, Integer> getModuleMap() {
		Map<Integer, Integer> moduleMap = new HashMap();
		QueryDefinition query = new QueryDefinition("WebModule");
		query.select("WebModule", "ModuleType");
		query.select("WebModule", "WorkflowObjectID");
		QueryBuilder qb = Database.getQueryBuilder();
		SqlRowSet rowSet = this.operations.queryForRowSet(qb.toSql(query));

		while (rowSet.next()) {
			if (!moduleMap.containsKey(rowSet.getInt("WorkflowObjectID"))) {
				moduleMap.put(rowSet.getInt("WorkflowObjectID"), rowSet.getInt("ModuleType"));
			}
		}

		return moduleMap;
	}

	private static class Columns {
		public static final String MODULE_TYPE = "ModuleType";
		public static final String WORKFLOW_OBJECT_ID = "WorkflowObjectID";
	}
}
