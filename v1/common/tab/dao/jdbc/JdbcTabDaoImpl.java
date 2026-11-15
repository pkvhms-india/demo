package com.varicent.common.tab.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.varicent.common.payee.domain.Payee;
import com.varicent.common.query.domain.Constraint;
import com.varicent.common.query.domain.DataField;
import com.varicent.common.query.domain.Join;
import com.varicent.common.query.domain.Literal;
import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.query.domain.SelectItem;
import com.varicent.common.query.sql.QueryBuilder;
import com.varicent.common.tab.dao.TabDao;
import com.varicent.common.tab.domain.Tab;
import com.varicent.core.database.Database;
import com.varicent.core.database.support.RowMapperUtil;
import com.varicent.security.domain.UserAccount;
import com.varicent.workflow.dao.jdbc.WorkflowQuery;
import com.varicent.workflow.domain.WorkflowObject;

@Repository("tabDao")
public class JdbcTabDaoImpl implements TabDao {
	private static final String TABLE_NAME = "WebTab";
	private final RowMapper<Tab> tabMapper = new RowMapper<Tab>() {
		@Override
		public Tab mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
			int id = resultSet.getInt("ID");
			Integer parentId = this.getInteger(resultSet, "Parent");
			Integer previousId = this.getInteger(resultSet, "Prev");
			String name = resultSet.getString("Name");
			Tab.Type type = Tab.Type.getType(resultSet.getInt("Type"));
			int workflowObjectId = resultSet.getInt("WorkflowObjectID");
			return new Tab(id, parentId, previousId, name, type, workflowObjectId);
		}

		private Integer getInteger(ResultSet resultSet, String columnName) throws SQLException {
			Integer integer = null;
			int intValue = resultSet.getInt(columnName);
			if (!resultSet.wasNull()) {
				integer = new Integer(intValue);
			}

			return integer;
		}
	};
	private final JdbcOperations operations;

	@Autowired
	public JdbcTabDaoImpl(DataSource dataSource) {
		this.operations = Database.getOperations(dataSource);
	}

	@Override
	public String getObjectId(WorkflowObject.Type type, int workflowObjectId) {
		DataField field = WorkflowQuery.getWorkflowObjectField(type);
		String table = field.getTable();
		String column = field.getColumn();
		QueryDefinition objectQuery = new QueryDefinition(table);
		objectQuery.select(table, column);
		objectQuery.newConstraint(table, "WorkflowObjectID", Constraint.Operator.Equals, workflowObjectId);
		QueryBuilder queryBuilder = Database.getQueryBuilder();
		List<String> result = this.operations.query(queryBuilder.toSql(objectQuery), RowMapperUtil.getStringMapper());
		return result.isEmpty() ? null : (String) result.get(0);
	}

	@Override
	public Map<Integer, WorkflowObject> loadObjectIDs() {
		Map<Integer, WorkflowObject> objectMap = new HashMap();
		QueryDefinition compPlans = new QueryDefinition("CompPlan");
		Join compPlanJoin = new Join("WebTab", Join.Type.INNER);
		compPlanJoin.newConstraint("CompPlan", "WorkflowObjectID", Constraint.Operator.Equals, "WebTab",
				"WorkflowObjectID");
		compPlans.joinWith(compPlanJoin);
		compPlans.select("CompPlan", "WorkflowObjectID");
		compPlans.select("CompPlan", "CompPlanID", "ID");
		compPlans.select(Literal.newLiteral(1), "ObjectType");
		compPlans.select("CompPlan", "Name");
		QueryDefinition reports = new QueryDefinition("TailoredReport");
		Join reportJoin = new Join("WebTab", Join.Type.INNER);
		reportJoin.newConstraint("TailoredReport", "WorkflowObjectID", Constraint.Operator.Equals, "WebTab",
				"WorkflowObjectID");
		reports.joinWith(reportJoin);
		reports.select("TailoredReport", "WorkflowObjectID");
		reports.select("TailoredReport", "ID", "ID");
		reports.select(Literal.newLiteral(2), "ObjectType");
		reports.select("TailoredReport", "Name");
		QueryDefinition webForms = new QueryDefinition("WebForm");
		Join webFormJoin = new Join("WebTab", Join.Type.INNER);
		webFormJoin.newConstraint("WebForm", "WorkflowObjectID", Constraint.Operator.Equals, "WebTab",
				"WorkflowObjectID");
		webForms.joinWith(webFormJoin);
		webForms.select("WebForm", "WorkflowObjectID");
		webForms.select("WebForm", "WebFormID", "ID", SelectItem.Type.STRING);
		webForms.select(Literal.newLiteral(4), "ObjectType");
		webForms.select("WebForm", "Name");
		QueryDefinition webReports = new QueryDefinition("WebReport");
		Join webReportJoin = new Join("WebTab", Join.Type.INNER);
		webReportJoin.newConstraint("WebReport", "WorkflowObjectID", Constraint.Operator.Equals, "WebTab",
				"WorkflowObjectID");
		webReports.joinWith(webReportJoin);
		webReports.select("WebReport", "WorkflowObjectID");
		webReports.select("WebReport", "WebReportID", "ID", SelectItem.Type.STRING);
		webReports.select(Literal.newLiteral(5), "ObjectType");
		webReports.select("WebReport", "Name");
		QueryDefinition drawAssign = new QueryDefinition("DrawAssignmentWebSettings");
		Join drawAssignJoin = new Join("WebTab", Join.Type.INNER);
		drawAssignJoin.newConstraint("DrawAssignmentWebSettings", "WorkflowObjectID", Constraint.Operator.Equals,
				"WebTab", "WorkflowObjectID");
		drawAssign.joinWith(drawAssignJoin);
		Join drawBlockJoin = new Join("BaseBlock", Join.Type.INNER);
		drawBlockJoin.newConstraint("DrawAssignmentWebSettings", "BlockID", Constraint.Operator.Equals, "BaseBlock",
				"BlockID");
		drawAssign.joinWith(drawBlockJoin);
		drawAssign.select("DrawAssignmentWebSettings", "WorkflowObjectID");
		drawAssign.select("DrawAssignmentWebSettings", "BlockID", "ID", SelectItem.Type.STRING);
		drawAssign.select(Literal.newLiteral(7), "ObjectType");
		drawAssign.select("BaseBlock", "Name");
		QueryDefinition drawReport = new QueryDefinition("DrawReportWebSettings");
		Join drawReportJoin = new Join("WebTab", Join.Type.INNER);
		drawReportJoin.newConstraint("DrawReportWebSettings", "WorkflowObjectID", Constraint.Operator.Equals, "WebTab",
				"WorkflowObjectID");
		drawReport.joinWith(drawReportJoin);
		Join drawReportBlockJoin = new Join("BaseBlock", Join.Type.INNER);
		drawReportBlockJoin.newConstraint("DrawReportWebSettings", "BlockID", Constraint.Operator.Equals, "BaseBlock",
				"BlockID");
		drawReport.joinWith(drawReportBlockJoin);
		drawReport.select("DrawReportWebSettings", "WorkflowObjectID");
		drawReport.select("DrawReportWebSettings", "BlockID", "ID", SelectItem.Type.STRING);
		drawReport.select(Literal.newLiteral(8), "ObjectType");
		drawReport.select("BaseBlock", "Name");
		QueryDefinition webTabs = new QueryDefinition("WebTab");
		webTabs.newConstraint("WebTab", "Type", Constraint.Operator.Equals, 0);
		webTabs.select("WebTab", "WorkflowObjectID");
		webTabs.select("WebTab", "ID", "ID", SelectItem.Type.STRING);
		webTabs.select(Literal.newLiteral(6), "ObjectType");
		webTabs.select("WebTab", "Name");
		QueryDefinition compPlanManager = new QueryDefinition("Pulse");
		Join compPlanManagerJoin = new Join("WebTab", Join.Type.INNER);
		compPlanManagerJoin.newConstraint("Pulse", "WorkflowObjectID", Constraint.Operator.Equals, "WebTab",
				"WorkflowObjectID");
		compPlanManager.joinWith(compPlanManagerJoin);
		compPlanManager.select("Pulse", "WorkflowObjectID");
		compPlanManager.select("Pulse", "ID", "ID", SelectItem.Type.STRING);
		compPlanManager.select(Literal.newLiteral(9), "ObjectType");
		compPlanManager.select("Pulse", "Name");
		compPlans.union(reports);
		reports.union(webForms);
		webForms.union(webReports);
		webReports.union(drawAssign);
		drawAssign.union(drawReport);
		drawReport.union(webTabs);
		webTabs.union(compPlanManager);
		QueryBuilder qb = Database.getQueryBuilder();
		SqlRowSet rowSet = this.operations.queryForRowSet(qb.toSql(compPlans));

		while (rowSet.next()) {
			WorkflowObject obj = new WorkflowObject(rowSet.getInt("WorkflowObjectID"),
					WorkflowObject.Type.values()[rowSet.getInt("ObjectType")], rowSet.getString("ID"),
					rowSet.getString("Name"));
			if (!objectMap.containsKey(obj.getObjectId())) {
				objectMap.put(obj.getObjectId(), obj);
			}
		}

		return objectMap;
	}

	@Override
	public List<Tab> getTabs() {
		QueryDefinition tabQuery = new QueryDefinition("WebTab");
		tabQuery.select("WebTab", "ID");
		tabQuery.select("WebTab", "Parent");
		tabQuery.select("WebTab", "Prev");
		tabQuery.select("WebTab", "Name");
		tabQuery.select("WebTab", "Type");
		tabQuery.select("WebTab", "WorkflowObjectID");
		QueryBuilder queryBuilder = Database.getQueryBuilder();
		return this.operations.query(queryBuilder.toSql(tabQuery), this.tabMapper);
	}

	@Override
	public List<Integer> getViewableTabIds() {
		UserAccount user = UserAccount.getCurrent();
		QueryDefinition tabIdQuery = WorkflowQuery.getViewableObjectsQuery(user.getPayeeId(), WorkflowObject.Type.TAB);
		QueryBuilder queryBuilder = Database.getQueryBuilder();
		return this.operations.query(queryBuilder.toSql(tabIdQuery), RowMapperUtil.getIntegerMapper());
	}

	@Override
	public List<Integer> getViewableTabIds(Payee user) {
		QueryDefinition tabIdQuery = WorkflowQuery.getViewableObjectsQuery(user.getId(), WorkflowObject.Type.TAB);
		QueryBuilder queryBuilder = Database.getQueryBuilder();
		return this.operations.query(queryBuilder.toSql(tabIdQuery), RowMapperUtil.getIntegerMapper());
	}

	private static class Columns {
		public static final String ID = "ID";
		public static final String PARENT = "Parent";
		public static final String PREV = "Prev";
		public static final String NAME = "Name";
		public static final String TYPE = "Type";
		public static final String WORKFLOW_OBJECT_ID = "WorkflowObjectID";
	}
}
