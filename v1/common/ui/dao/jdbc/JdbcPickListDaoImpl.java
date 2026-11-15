package com.varicent.common.ui.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;
import javax.xml.stream.XMLStreamException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.varicent.common.query.domain.Constraint;
import com.varicent.common.query.domain.Join;
import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.query.domain.SourceDefinition;
import com.varicent.common.query.sql.QueryBuilder;
import com.varicent.common.query.xml.SourceDefinitionParser;
import com.varicent.common.reference.dao.ReferenceDao;
import com.varicent.common.reference.domain.Reference;
import com.varicent.common.ui.dao.PickListDao;
import com.varicent.common.ui.domain.SelectOption;
import com.varicent.core.database.Database;
import com.varicent.core.database.support.RowMapperUtil;
import com.varicent.core.text.Format;
import com.varicent.reporting.dao.jdbc.BaseBufferMerger;
import com.varicent.reporting.domain.PicklistDisplayOptions;
import com.varicent.security.domain.UserAccount;
import com.varicent.webform.dao.jdbc.BufferMerger;
import com.varicent.workflow.dao.jdbc.WorkflowQuery;

@Repository("pickListDao")
public class JdbcPickListDaoImpl implements PickListDao {
	private final JdbcOperations operations;
	private final ReferenceDao referenceDao;
	private final String FilterPrefix = "_Filter_";

	@Autowired
	public JdbcPickListDaoImpl(DataSource ds, ReferenceDao referenceDao) {
		this.operations = Database.getOperations(ds);
		this.referenceDao = referenceDao;
	}

	@Override
	public List<SelectOption> getOptions(String table, String column) {
		return this.getOptions(table, column, (SourceDefinition) null);
	}

	@Override
	public List<SelectOption> getWebFormOptions(String table, String column, BufferMerger bufferMerger) {
		return this.getOptions(table, column, bufferMerger, (BaseBufferMerger) null, (SourceDefinition) null,
				(PicklistDisplayOptions) null);
	}

	@Override
	public List<SelectOption> getOptions(String table, String column, SourceDefinition filter) {
		return this.getOptions(table, column, (BufferMerger) null, (BaseBufferMerger) null, filter,
				(PicklistDisplayOptions) null);
	}

	@Override
	public List<SelectOption> getWebReportOptions(String table, String column, SourceDefinition filter,
			BaseBufferMerger merger, PicklistDisplayOptions options) {
		return this.getOptions(table, column, (BufferMerger) null, merger, filter, options);
	}

	List<SelectOption> getOptions(String table, String column, BufferMerger bufferMerger, BaseBufferMerger reportMerger,
			SourceDefinition filter, PicklistDisplayOptions displayOptions) {
		if (table.equals("Account_") && column.equals("DrCr_")) {
			List<SelectOption> list = new ArrayList(3);
			list.add(new SelectOption("Debit", "Debit"));
			list.add(new SelectOption("Credit", "Credit"));
			return list;
		} else {
			QueryBuilder qb = Database.getQueryBuilder();
			QueryDefinition listQuery = new QueryDefinition("CustomPickList");
			listQuery.select("CustomPickList", "Name");
			listQuery.select("CustomPickListFilter", "Query");
			Join join = new Join("CustomPickListFilter", Join.Type.LEFT);
			join.newConstraint("CustomPickList", "FilterID", Constraint.Operator.Equals, "CustomPickListFilter", "ID");
			listQuery.joinWith(join);
			listQuery.newConstraint("CustomPickList", "TableName", Constraint.Operator.Equals, table);
			listQuery.newConstraint("CustomPickList", "ColumnName", Constraint.Operator.Equals, column);
			List<Map<String, Object>> listResults = this.operations.queryForList(qb.toSql(listQuery));
			Reference reference = this.referenceDao.getReference(table, column);
			if (reference == null) {
				return null;
			} else {
				String name = null;
				String query = null;
				if (listResults.isEmpty()) {
					name = reference.getReferencedColumn();
				} else {
					Map<String, Object> listResult = listResults.get(0);
					name = (String) listResult.get("Name");
					query = (String) listResult.get("Query");
				}

				if (displayOptions != null) {
					name = displayOptions.getDescription();
				}

				String queryObj = query;
				String sourceTable = reference.getReferencedTable();
				String sourceColumn = reference.getReferencedColumn();

				QueryDefinition optionQuery;
				try {
					if (queryObj == null) {
						optionQuery = new QueryDefinition(sourceTable);
					} else {
						optionQuery = SourceDefinitionParser.getSourceDefinition(queryObj).getQuery();
					}
				} catch (XMLStreamException e) {
					throw new RuntimeException(e);
				}

				optionQuery.selectDistinct();
				optionQuery.select(sourceTable, sourceColumn);
				if (optionQuery.getSource().isQuery()
						&& !optionQuery.getSource().getQuery().HasSelected(sourceTable, sourceColumn)) {
					optionQuery.getSource().getQuery().select(sourceTable, sourceColumn);
				}

				if (name != null) {
					optionQuery.select(sourceTable, name);
					if (optionQuery.getSource().isQuery()
							&& !optionQuery.getSource().getQuery().HasSelected(sourceTable, name)) {
						optionQuery.getSource().getQuery().select(sourceTable, name);
					}
				}

				optionQuery.newNullConstraint(sourceTable, sourceColumn, false);
				if (bufferMerger != null) {
					bufferMerger.mergeWithBufferUpdate(optionQuery);
				} else if (reportMerger != null) {
					reportMerger.mergeWithBufferUpdate(optionQuery);
				}

				if (filter != null) {
					String filterAliasName = "_Filter_" + filter.getMainSource();
					Join innerJoin = new Join(filter.getQuery(), filterAliasName, Join.Type.INNER);
					innerJoin.newConstraint(sourceTable, sourceColumn, Constraint.Operator.Equals, filterAliasName,
							sourceColumn);
					optionQuery.joinWith(innerJoin);
				}

				boolean isEffective = this.isEffectiveDated(sourceTable);
				QueryDefinition outerQuery = new QueryDefinition();
				if (isEffective) {
					optionQuery.select(sourceTable, "EffStart_");
					outerQuery = new QueryDefinition(optionQuery, "allData");
					outerQuery.select("allData", sourceColumn);
					if (name != null) {
						outerQuery.select("allData", name);
					}

					QueryDefinition groupedQuery = new QueryDefinition(sourceTable);
					groupedQuery.select(sourceTable, sourceColumn);
					groupedQuery.selectMin(sourceTable, "EffStart_", "minStart");
					groupedQuery.groupBy(sourceColumn);
					Join joinGrouped = new Join(groupedQuery, "groupedQuery", Join.Type.INNER);
					joinGrouped.newConstraint("allData", "EffStart_", Constraint.Operator.Equals, "groupedQuery",
							"minStart");
					joinGrouped.newConstraint("allData", sourceColumn, Constraint.Operator.Equals, "groupedQuery",
							sourceColumn);
					outerQuery.joinWith(joinGrouped);
					if (displayOptions == null) {
						outerQuery.orderBy("allData", sourceColumn);
					} else {
						Boolean sortDescending = displayOptions
								.getOrder() == PicklistDisplayOptions.SortOrder.Descending;
						if (displayOptions.getSortByID()) {
							outerQuery.orderBy("allData", sourceColumn, sortDescending);
						} else {
							outerQuery.orderBy("allData", name, sortDescending);
						}
					}
				} else if (displayOptions == null) {
					optionQuery.orderBy(sourceTable, sourceColumn);
				} else {
					Boolean sortDescending = displayOptions.getOrder() == PicklistDisplayOptions.SortOrder.Descending;
					if (displayOptions.getSortByID()) {
						optionQuery.orderBy(sourceTable, sourceColumn, sortDescending);
					} else {
						optionQuery.orderBy(sourceTable, name, sortDescending);
					}
				}

				return this.operations.query(qb.toSql(isEffective ? outerQuery : optionQuery),
						new RowMapper<SelectOption>() {
							@Override
							public SelectOption mapRow(ResultSet rs, int rowNumber) throws SQLException {
								Object value = rs.getObject(1);
								Object name = rs.getObject(2);

								return new SelectOption(name == null ? null : Format.asValueString(name),
										Format.asValueString(value));
							}
						});
			}
		}
	}

	boolean isEffectiveDated(String table) {
		QueryDefinition query = new QueryDefinition("CustomTable");
		query.select("CustomTable", "EffectiveDated");
		query.newConstraint("CustomTable", "TableName", Constraint.Operator.Equals, table);
		QueryBuilder qb = Database.getQueryBuilder();
		return this.operations.queryForObject(qb.toSql(query), Integer.class) == 1;
	}

	@Override
	public List<SelectOption> getWorkflowOptions(String planId) {
		UserAccount user = UserAccount.getCurrent();
		QueryDefinition query = WorkflowQuery.getAuthorizedPayeeFullQuery(planId, user.getPayeeId());
		QueryBuilder qb = Database.getQueryBuilder();
		Set<SelectOption> options = new LinkedHashSet();
		options.add(new SelectOption(user.getName(), user.getPayeeId()));
		options.addAll(this.operations.query(qb.toSql(query), RowMapperUtil.getOptionMapper()));
		return new ArrayList(options);
	}
}
