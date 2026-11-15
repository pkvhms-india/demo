package com.varicent.common.history.dao.jdbc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.varicent.common.history.dao.HistoryDao;
import com.varicent.common.query.domain.Clause;
import com.varicent.common.query.domain.Constraint;
import com.varicent.common.query.domain.InsertDefinition;
import com.varicent.common.query.domain.Join;
import com.varicent.common.query.domain.OrClause;
import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.query.domain.SqlDataSource;
import com.varicent.common.query.domain.UpdateDefinition;
import com.varicent.common.query.sql.QueryBuilder;
import com.varicent.common.query.sql.insert.InsertBuilder;
import com.varicent.common.query.sql.update.UpdateBuilder;
import com.varicent.common.schema.dao.SchemaDao;
import com.varicent.core.database.Database;
import com.varicent.core.database.schema.ColumnDefinition;
import com.varicent.core.database.schema.TableDefinition;
import com.varicent.core.database.support.RowMapperUtil;
import com.varicent.security.domain.UserAccount;

@Repository("historyDao")
public class JdbcHistoryDaoImpl implements HistoryDao {
	private static final List<String> SYSTEM_VIEWS = Arrays.asList("CompPlan", "Earnings_", "NetAdjustments_",
			"NetEarnings_", "NetPending_", "Payment_", "Pending_");
	private static final List<String> STATIC_HISTORY_TABLES = Arrays.asList("Reference", "ReferenceColumn", "Time_",
			"TimeCalendar", "TimeLevel", "TimeLock", "GroupMember", "PayeeGroup_", "CustomTable", "CustomColumn",
			"Account_", "CustomPickList", "PickListParam", "WorkflowGroup", "WorkflowGroupTree",
			"WorkflowGroupTreeNode", "WorkflowGroupTreeEdge", "Calculation", "CalculationPartition",
			"CalculationConstraintTree", "CalculationConstraintValue", "CalculationOperand", "CalculationJoin",
			"CalculationLookup", "CalculationUnion", "CalculationUnionColumn", "CalculationTrace",
			"CalculationReference", "ReferenceTime", "CalculationData", "Report", "ReportCategory", "ReportResult",
			"ReportSort", "ReportTrace", "ReportTraceColumn", "ReportTraceData", "DrawDefinition", "DrawTemplate",
			"DrawAssignmentOngoingTerm", "DrawAssignmentFixedTerm", "DrawAssignmentFixedTermAmount", "DrawSchedule",
			"Sort", "SortSource", "TimeShift", "TimeShiftSource", "PortDefinition", "ComposerPort",
			"ComposerPortColumn");
	private final JdbcOperations operations;

	@Autowired
	public JdbcHistoryDaoImpl(DataSource ds) {
		this.operations = Database.getOperations(ds);
	}

	@Override
	public void assignRevision() {
		UpdateDefinition update = new UpdateDefinition("HistoryRevision");
		update.set("AdminID", UserAccount.getCurrent().getPayeeId());
		update.crossJoin(ActiveRevision.TABLE_NAME);
		update.newConstraint("HistoryRevision", "RevisionID", Constraint.Operator.Equals, ActiveRevision.TABLE_NAME,
				"RevisionID");
		UpdateBuilder ub = Database.getUpdateBuilder();
		this.operations.update(ub.toSql(update));
	}

	@Override
	public void beginRevision() {
		if (Database.getSyntax().isSqlServer()) {
			this.operations.update(ActiveRevision.CREATE_TABLE_SQL);
		}

	}

	@Override
	public void endRevision() {
		if (Database.getSyntax().isSqlServer()) {
			this.operations.update(ActiveRevision.DROP_TABLE_SQL);
		}

	}

	public QueryDefinition getHistoryDataQuery(String currentTable, int revision, SchemaDao schemaDao) {
		return this.getHistoryDataQuery(this.getHistoryTable(currentTable, revision, schemaDao), revision);
	}

	@Override
	public QueryDefinition getHistoryDataQuery(TableDefinition historyTable, int revision) {
		String historyTableName = historyTable.getName();
		List<ColumnDefinition> historyColumns = historyTable.getColumns();
		QueryDefinition query = new QueryDefinition(historyTableName);
		this.restrictToRevision(query, historyTableName, revision);

		for (ColumnDefinition column : historyColumns) {
			query.select(historyTableName, column.getName());
		}

		return query;
	}

	@Override
	public TableDefinition getHistoryTable(String currentTable, int revision, SchemaDao schemaDao) {
		Assert.isTrue(this.isStaticHistoryTable(currentTable));
		String historyTable = this.getStaticHistoryTableName(currentTable);
		List<ColumnDefinition> columns = schemaDao.getColumns(historyTable);
		return new TableDefinition(historyTable, columns);
	}

	public String getStaticHistoryTableName(String currentTable) {
		Assert.isTrue(this.isStaticHistoryTable(currentTable));
		return "_History" + currentTable;
	}

	public String getHistoryTableName(int historyId) {
		return "_History" + historyId;
	}

	@Override
	public int getNewRevision() {
		InsertDefinition insert = new InsertDefinition("HistoryRevision", new String[] { "AdminID", "StartedAt" });
		insert.addValue(UserAccount.getCurrent().getPayeeId());
		insert.addSystemDateTime();
		InsertBuilder ib = Database.getInsertBuilder();
		this.operations.update(ib.toSql(insert));
		return this.getLatestRevision();
	}

	@Override
	public int getLatestRevision() {
		QueryDefinition query = new QueryDefinition("HistoryRevision");
		query.selectMax("HistoryRevision", "RevisionID", "RevisionID");
		QueryBuilder qb = Database.getQueryBuilder();
		qb.setReadPast(true);
		return (Integer) this.operations.queryForObject(qb.toSql(query), Integer.class);
	}

	@Override
	public Date getRevisionTimestamp(int revision) {
		QueryDefinition query = new QueryDefinition("HistoryRevision");
		query.select("HistoryRevision", "StartedAt");
		query.newConstraint("HistoryRevision", "RevisionID", Constraint.Operator.Equals, revision);
		QueryBuilder qb = Database.getQueryBuilder();
		return (Date) this.operations.queryForObject(qb.toSql(query), RowMapperUtil.getDateMapper());
	}

	@Override
	public boolean isSystemView(String name) {
		return SYSTEM_VIEWS.contains(name);
	}

	@Override
	public boolean isStaticHistoryTable(String name) {
		return STATIC_HISTORY_TABLES.contains(name);
	}

	public boolean hasHistoryTable(String name, int revision) {
		if (name.startsWith("_Result")) {
			return false;
		} else if (this.isSystemView(name)) {
			return false;
		} else {
			return this.isStaticHistoryTable(name);
		}
	}

	@Override
	public QueryDefinition historifyQuery(QueryDefinition query, int revision, SchemaDao schemaDao) {
		if (query.getSource() == null) {
			return query;
		} else {
			String alias = query.getSource().hasAlias() ? query.getSource().getAlias() : query.getSource().getTable();
			if (query.getSource().isQuery()) {
				query.setSource(new SqlDataSource(
						this.historifyQuery(query.getSource().getQuery(), revision, schemaDao), alias));
			} else {
				String table = query.getSource().getTable();
				if (this.hasHistoryTable(table, revision)) {
					query.setSource(new SqlDataSource(
							this.getHistoryDataQuery(query.getSource().getTable(), revision, schemaDao), alias));
				}
			}

			for (Join join : query.getJoins()) {
				String joinAlias = join.getSource().hasAlias() ? join.getSource().getAlias()
						: join.getSource().getTable();
				if (join.getSource().isQuery()) {
					join.setSource(new SqlDataSource(
							this.historifyQuery(join.getSource().getQuery(), revision, schemaDao), joinAlias));
				} else {
					String table = join.getSource().getTable();
					if (this.hasHistoryTable(table, revision)) {
						join.setSource(new SqlDataSource(
								this.getHistoryDataQuery(join.getSource().getTable(), revision, schemaDao), joinAlias));
					}
				}
			}

			if (query.hasUnion()) {
				query.setUnionWith(this.historifyQuery(query.getUnion(), revision, schemaDao));
			}

			return query;
		}
	}

	private void restrictToRevision(QueryDefinition query, String historyTable, int revision) {
		query.newConstraint(historyTable, "_Start", Constraint.Operator.LessThanEquals, revision);
		Clause clause = new OrClause();
		clause.newConstraint(historyTable, "_End", Constraint.Operator.GreaterThan, revision);
		clause.newNullConstraint(historyTable, "_End", true);
		query.addConstraint(clause);
	}
}
