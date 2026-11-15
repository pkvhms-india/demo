package com.varicent.common.query.sql;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.varicent.common.query.domain.CommonTableExpression;
import com.varicent.common.query.domain.GroupItem;
import com.varicent.common.query.domain.ISelectItem;
import com.varicent.common.query.domain.Join;
import com.varicent.common.query.domain.OrderItem;
import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.query.domain.SelectAggregate;
import com.varicent.common.query.domain.SelectAll;
import com.varicent.common.query.domain.SelectItem;
import com.varicent.common.query.domain.SelectLiteral;
import com.varicent.common.query.domain.SelectTableAll;
import com.varicent.common.query.domain.TopLevelQueryDefinition;
import com.varicent.common.query.domain.Variable;
import com.varicent.core.database.portability.SqlSyntax;

public class QueryBuilder {
	private final SqlSyntax syntax;
	private final SqlBuilder builder;
	private boolean ReadPast;

	public QueryBuilder(SqlSyntax syntax) {
		this.syntax = syntax;
		this.builder = new SqlBuilder(syntax);
		this.ReadPast = false;
	}

	public void setReadPast(boolean readPast) {
		this.ReadPast = readPast;
	}

	public String toSql(TopLevelQueryDefinition topQuery) {
		StringBuilder queryStringBuilder = new StringBuilder();
		if (topQuery.getVariable() != null) {
			queryStringBuilder.append(this.getVariableDeclarationSql(topQuery.getVariable()));
		}

		queryStringBuilder.append(this.getCommonTableExpressionSql(topQuery.getCommonTableExpressions()));
		queryStringBuilder.append(this.toSql(topQuery.getQuery()));
		return queryStringBuilder.toString();
	}

	private String getVariableDeclarationSql(Variable variable) {
		StringBuilder sqlStringBuilder = new StringBuilder();
		sqlStringBuilder.append("DECLARE @" + variable.getName() + " nvarchar(100);");
		sqlStringBuilder.append("SET @" + variable.getName() + " = '" + variable.getInitialValue().toString() + "';");
		return sqlStringBuilder.toString();
	}

	private String getCommonTableExpressionSql(List<CommonTableExpression> ctes) {
		StringBuilder sb = new StringBuilder();
		sb.append("WITH ");
		List<String> cteSqlStrings = new ArrayList();

		for (int i = 0; i < ctes.size(); ++i) {
			StringBuilder currentCteStringBuilder = new StringBuilder();
			CommonTableExpression cte = ctes.get(i);
			if (cte != null) {
				currentCteStringBuilder.append(this.syntax.quote(cte.getExpressionName()));
				List<String> columnNames = cte.getColumnNames();
				if (columnNames != null) {
					List<String> quotedColumnNames = new ArrayList();

					for (String columnName : columnNames) {
						quotedColumnNames.add(this.syntax.quote(columnName));
					}

					currentCteStringBuilder.append(" (");
					currentCteStringBuilder.append(StringUtils.collectionToCommaDelimitedString(quotedColumnNames));
					currentCteStringBuilder.append(")");
				}

				currentCteStringBuilder.append(" AS (");
				currentCteStringBuilder.append(cte.getQueryDefinition().toSql());
				currentCteStringBuilder.append(") ");
				cteSqlStrings.add(currentCteStringBuilder.toString());
			}
		}

		sb.append(StringUtils.collectionToCommaDelimitedString(cteSqlStrings));
		return sb.toString();
	}

	public String toSql(QueryDefinition query) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		if (query.isSelectDistinct()) {
			sb.append("DISTINCT ");
		}

		Dictionary<ISelectItem, SelectItemInfo> remappedSelectInfo = null;
		if (query.getLimit() > 0 && this.syntax.isSqlServer()) {
			sb.append("TOP ");
			sb.append(query.getLimit());
			sb.append(" ");
		}

		if (query.getSelect().isEmpty()) {
			sb.append("*");
		} else {
			List<String> itemSql = new ArrayList();

			for (ISelectItem item : query.getSelect()) {
				itemSql.add(this.builder.getSql(item));
			}

			sb.append(StringUtils.collectionToCommaDelimitedString(itemSql));
		}

		if (query.getSource() != null) {
			sb.append(" FROM ");
			sb.append(this.builder.getSql(query.getSource()));
		}

		if (!query.getJoins().isEmpty()) {
			List<String> joinSql = new ArrayList();

			for (Join join : query.getJoins()) {
				joinSql.add(this.builder.getSql(join));
			}

			sb.append(" ");
			sb.append(StringUtils.collectionToDelimitedString(joinSql, " "));
		}

		if (query.getRestriction().hasConstraints()) {
			sb.append(" WHERE ");
			sb.append(this.builder.getSql(query.getRestriction()));
		}

		if (!query.getGroup().isEmpty()) {
			sb.append(" GROUP BY ");
			List<String> groupSql = new ArrayList();

			for (GroupItem item : query.getGroup()) {
				groupSql.add(this.builder.getSql(item));
			}

			sb.append(StringUtils.collectionToCommaDelimitedString(groupSql));
		}

		if (query.getHaving() != null && query.getHaving().hasConstraints()) {
			sb.append("HAVING ");
			sb.append(this.builder.getSql(query.getHaving()));
		}

		if (!query.getOrder().isEmpty()) {
			sb.append(" ORDER BY ");
			List<String> orderSql = new ArrayList();

			for (OrderItem item : query.getOrder()) {
				orderSql.add(this.builder.getSql(item));
			}

			sb.append(StringUtils.collectionToCommaDelimitedString(orderSql));
		}

		if (query.hasUnion()) {
			sb.append(" UNION ALL ");
			sb.append(this.toSql(query.getUnion()));
		}

		if (query.getRecursion() > 1) {
			sb.append(" OPTION(MAXRECURSION " + query.getRecursion() + ")");
		}

		if (this.ReadPast) {
			sb.append(" WITH (READPAST)");
		}

		return sb.toString();
	}

	private Dictionary<ISelectItem, SelectItemInfo> getSelectInfoForRowNum(List<ISelectItem> selectItems) {
		Dictionary<ISelectItem, SelectItemInfo> toReturn = new Hashtable();
		int currIndex = 1;

		for (ISelectItem item : selectItems) {
			SelectItemInfo itemInfo;
			if (item instanceof SelectAll) {
				itemInfo = new SelectItemInfo("", "", true, false);
			} else if (item instanceof SelectItem) {
				SelectItem selectItem = (SelectItem) item;
				itemInfo = new SelectItemInfo(selectItem.hasAlias() ? selectItem.getAlias() : selectItem.getColumn(),
						String.valueOf(currIndex), false, false);
			} else if (item instanceof SelectLiteral) {
				SelectLiteral selectLiteral = (SelectLiteral) item;
				itemInfo = new SelectItemInfo(
						selectLiteral.hasAlias() ? selectLiteral.getAlias()
								: this.builder.getSql(selectLiteral.getValue()),
						String.valueOf(currIndex), false, false);
			} else if (item instanceof SelectAggregate) {
				SelectAggregate selectAggregate = (SelectAggregate) item;
				itemInfo = new SelectItemInfo(
						selectAggregate.hasAlias() ? selectAggregate.getAlias() : selectAggregate.getColumn(),
						String.valueOf(currIndex), false, false);
			} else {
				if (!(item instanceof SelectTableAll)) {
					Assert.state(false);
					return null;
				}

				SelectTableAll selectTableAll = (SelectTableAll) item;
				itemInfo = new SelectItemInfo(selectTableAll.getTable(), "", false, true);
			}

			toReturn.put(item, itemInfo);
			++currIndex;
		}

		return toReturn;
	}

	private ISelectItem overrideAlias(ISelectItem item, String newAlias) {
		if (item instanceof SelectAll) {
			return item;
		} else if (item instanceof SelectItem) {
			SelectItem selectItem = (SelectItem) item;
			return new SelectItem(selectItem.getTable(), selectItem.getColumn(), newAlias, selectItem.getType());
		} else if (item instanceof SelectLiteral) {
			SelectLiteral selectLiteral = (SelectLiteral) item;
			return new SelectLiteral(selectLiteral.getValue(), newAlias);
		} else if (item instanceof SelectAggregate) {
			SelectAggregate selectAggregate = (SelectAggregate) item;
			return new SelectAggregate(selectAggregate.getType(), selectAggregate.getTable(),
					selectAggregate.getColumn(), newAlias);
		} else {
			Assert.state(false);
			return null;
		}
	}

	private String buildSelectClause(Dictionary<ISelectItem, SelectItemInfo> selectItemInfoMap,
			List<ISelectItem> selectItems) {
		if (selectItems.size() == 0) {
			return "*";
		} else {
			List<String> selects = new ArrayList();

			for (ISelectItem selectItem : selectItems) {
				SelectItemInfo item = selectItemInfoMap.get(selectItem);
				StringBuilder sb = new StringBuilder();
				if (item.isSelectAll()) {
					sb.append(" * ");
				} else if (item.isSelectTableAll()) {
					sb.append(this.syntax.quote(item.getOriginalName()));
					sb.append(".*");
				} else {
					sb.append(this.syntax.quote(item.getTemporaryName()));
					sb.append(" AS ");
					sb.append(this.syntax.quote(item.getOriginalName()));
				}

				selects.add(sb.toString());
			}

			return StringUtils.collectionToCommaDelimitedString(selects);
		}
	}

	private class SelectItemInfo {
		private final String originalName;
		private final String temporaryName;
		private final boolean selectAll;
		private final boolean selectTableAll;

		public String getOriginalName() {
			return this.originalName;
		}

		public String getTemporaryName() {
			return this.temporaryName;
		}

		public boolean isSelectAll() {
			return this.selectAll;
		}

		public boolean isSelectTableAll() {
			return this.selectTableAll;
		}

		public SelectItemInfo(String originalName, String temporaryName, boolean selectAll, boolean selectTableAll) {
			this.originalName = originalName;
			this.temporaryName = temporaryName;
			this.selectAll = selectAll;
			this.selectTableAll = selectTableAll;
		}
	}
}
