package com.varicent.common.query.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.util.Assert;

import com.varicent.common.query.sql.QueryBuilder;
import com.varicent.core.database.Database;

public class QueryDefinition {
	private final List<ISelectItem> select;
	private final List<Join> joins;
	private Clause restriction;
	private final List<GroupItem> group;
	private final List<OrderItem> order;
	private Clause having;
	private int limit;
	private boolean distinct;
	private SqlDataSource source;
	private QueryDefinition unionWith;
	private int recursion;

	public QueryDefinition() {
		this.source = null;
		this.select = new ArrayList();
		this.joins = new ArrayList();
		this.restriction = new AndClause();
		this.group = new ArrayList();
		this.order = new ArrayList();
		this.unionWith = null;
		this.having = new AndClause();
	}

	public QueryDefinition(QueryDefinition query) {
		this(query.source == null ? null : new SqlDataSource(query.source));

		for (ISelectItem item : query.select) {
			this.select.add(item.Copy());
		}

		for (Join join : query.joins) {
			this.joins.add(new Join(join));
		}

		this.restriction = (Clause) query.restriction.Copy();

		for (OrderItem item : query.order) {
			this.order.add(new OrderItem(item));
		}

		for (GroupItem item : query.group) {
			this.group.add(new GroupItem(item));
		}

		this.having = query.having;
		this.distinct = query.distinct;
		if (query.unionWith != null) {
			this.unionWith = new QueryDefinition(query.unionWith);
		}

		this.limit = query.limit;
		this.recursion = query.recursion;
	}

	public QueryDefinition(QueryDefinition query, String alias) {
		this(new SqlDataSource(query, alias));
	}

	public QueryDefinition(SqlDataSource source) {
		this.source = source;
		this.select = new ArrayList();
		this.joins = new ArrayList();
		this.restriction = new AndClause();
		this.group = new ArrayList();
		this.order = new ArrayList();
		this.unionWith = null;
		this.having = new AndClause();
	}

	public QueryDefinition(String table) {
		this(table, (String) null);
	}

	public QueryDefinition(String table, String alias) {
		this(new SqlDataSource(table, alias));
	}

	public void addConstraint(IConstraint constraint) {
		this.restriction.addConstraint(constraint);
	}

	public void addGroup(GroupItem item) {
		this.group.add(item);
	}

	public void addGroupItems(Collection<? extends GroupItem> items) {
		this.group.addAll(items);
	}

	public void addOrder(OrderItem item) {
		this.order.add(item);
	}

	public void addOrderItems(Collection<? extends OrderItem> items) {
		this.order.addAll(items);
	}

	public void addSelect(ISelectItem item) {
		this.select.add(item);
	}

	public void addSelects(Collection<? extends ISelectItem> items) {
		this.select.addAll(items);
	}

	public void crossJoin(QueryDefinition query, String alias) {
		Join join = new Join(query, alias, Join.Type.CROSS);
		this.joins.add(join);
	}

	public void crossJoin(SqlDataSource ds) {
		if (ds.getSourceType().equals(SqlDataSource.Type.QUERY)) {
			this.crossJoin(ds.getQuery(), ds.getAlias());
		} else if (ds.hasAlias()) {
			this.crossJoin(ds.getTable(), ds.getAlias());
		} else {
			this.crossJoin(ds.getTable());
		}

	}

	public void crossJoin(String table) {
		Join join = new Join(table, Join.Type.CROSS);
		this.joins.add(join);
	}

	public void crossJoin(String table, String alias) {
		Join join = new Join(table, alias, Join.Type.CROSS);
		this.joins.add(join);
	}

	public List<Join> getJoins() {
		return this.joins;
	}

	public void clearJoins() {
		this.joins.clear();
	}

	public void clearOrderBys() {
		this.order.clear();
	}

	public int getLimit() {
		return this.limit;
	}

	public int getRecursion() {
		return this.recursion;
	}

	public List<GroupItem> getGroup() {
		return this.group;
	}

	public Clause getHaving() {
		return this.having;
	}

	public List<OrderItem> getOrder() {
		return this.order;
	}

	public Clause getRestriction() {
		return this.restriction;
	}

	public List<ISelectItem> getSelect() {
		return this.select;
	}

	public SqlDataSource getSource() {
		return this.source;
	}

	public String getTableName(String sourceName) {
		if (this.source.getAlias() == sourceName) {
			if (this.source.getTable() == null) {
				if (this.source.getQuery().getSource() == null) {
					return sourceName;
				} else {
					return this.source.getQuery().getSource().hasAlias()
							? this.source.getQuery().getTableName(this.source.getQuery().getSource().getAlias())
							: this.source.getQuery().getSource().getTable();
				}
			} else {
				return this.source.getTable();
			}
		} else {
			for (Join join : this.joins) {
				if (sourceName == join.getSource().getAlias()) {
					if (join.getSource().getTable() != null) {
						return join.getSource().getTable();
					}

					QueryDefinition joinQuery = join.getSource().getQuery();
					if (joinQuery.getSource() == null) {
						return join.getSource().getAlias();
					}

					if (joinQuery.getSource().hasAlias()) {
						return joinQuery.getTableName(joinQuery.getSource().getAlias());
					}

					return joinQuery.getSource().getTable();
				}
			}

			return sourceName;
		}
	}

	public QueryDefinition getUnion() {
		return this.unionWith;
	}

	public boolean hasUnion() {
		return this.unionWith != null;
	}

	public boolean isSelectDistinct() {
		return this.distinct;
	}

	public void joinWith(Collection<? extends Join> joins) {
		this.joins.addAll(joins);
	}

	public void joinWith(Join join) {
		this.joins.add(join);
	}

	public void limitTo(int limit) {
		this.limit = limit;
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, BigDecimal value) {
		this.restriction.newConstraint(leftTable, leftColumn, operator, value);
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, Date value) {
		this.restriction.newConstraint(leftTable, leftColumn, operator, value);
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, Integer value) {
		this.restriction.newConstraint(leftTable, leftColumn, operator, value);
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, Variable value) {
		this.restriction.newConstraint(leftTable, leftColumn, operator, value);
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, Object value) {
		this.restriction.newConstraint(leftTable, leftColumn, operator, value);
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, String value) {
		this.restriction.newConstraint(leftTable, leftColumn, operator, value);
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, String rightTable,
			String rightColumn) {
		this.restriction.newConstraint(leftTable, leftColumn, operator, rightTable, rightColumn);
	}

	public void newConstraint(String leftTable, String leftColumn, Constraint.Operator operator, Timestamp value) {
		this.restriction.newConstraint(leftTable, leftColumn, operator, value);
	}

	public void newNullConstraint(String leftTable, String leftColumn, boolean isNull) {
		this.restriction.newNullConstraint(leftTable, leftColumn, isNull);
	}

	public void groupBy(String column) {
		this.groupBy((String) null, column);
	}

	public void groupBy(String table, String column) {
		this.group.add(new GroupItem(table, column));
	}

	public void orderBy(String column) {
		this.orderBy((String) null, column, false);
	}

	public void orderBy(String column, boolean descending) {
		this.orderBy((String) null, column, descending);
	}

	public void orderBy(String table, String column) {
		this.orderBy(table, column, false);
	}

	public void orderBy(String column, SelectAggregate.Type aggregator) {
		OrderAggregate item = new OrderAggregate((String) null, column, OrderItem.Direction.ASCENDING, aggregator);
		this.order.add(item);
	}

	public void having(String table, String column, Constraint.Operator op, Object value) {
		AndClause andClause = new AndClause();
		andClause.newConstraint(table, column, op, value);
		this.having.addConstraint(andClause);
	}

	public void orderBy(String table, String column, boolean descending) {
		OrderItem.Direction direction = descending ? OrderItem.Direction.DESCENDING : OrderItem.Direction.ASCENDING;
		OrderItem item = new OrderItem(table, column, direction);
		this.order.add(item);
	}

	public void select(Literal literal, String alias) {
		this.select.add(new SelectLiteral(literal, alias));
	}

	public void select(String table, String column) {
		this.select(table, column, (String) null);
	}

	public void select(String table, String column, String alias) {
		this.select(table, column, alias, SelectItem.Type.NONE);
	}

	public void select(String table, String column, String alias, SelectItem.Type type) {
		this.select.add(new SelectItem(table, column, alias, type));
	}

	public void selectAll() {
		this.select.add(new SelectAll());
	}

	public void selectCustom(String sqlServer, String alias) {
		this.select.add(new SelectCustom(sqlServer, alias));
	}

	public void selectCount() {
		this.select.add(new SelectAggregate(SelectAggregate.Type.COUNT));
	}

	public void selectDistinct() {
		this.distinct = true;
	}

	public void clearSelects() {
		this.select.clear();
	}

	public void selectMax(String table, String column, String alias) {
		this.select.add(new SelectAggregate(SelectAggregate.Type.MAX, table, column, alias));
	}

	public void selectMin(String table, String column, String alias) {
		this.select.add(new SelectAggregate(SelectAggregate.Type.MIN, table, column, alias));
	}

	public void selectSystemUtcTime(String alias) {
		this.select(Literal.newLiteral(Function.getDateTimeUtc()), alias);
	}

	public void selectSystemUtcDate(String alias) {
		this.select(Literal.newLiteral(Function.getDateUtc()), alias);
	}

	public void selectSystemTime(String alias) {
		this.select(Literal.newLiteral(Function.getDateTime()), alias);
	}

	public void selectSystemDate(String alias) {
		this.select(Literal.newLiteral(Function.getDate()), alias);
	}

	public void setRestriction(Clause restriction) {
		Assert.isTrue(restriction != null);
		this.restriction = restriction;
	}

	public void setSource(SqlDataSource source) {
		this.source = source;
	}

	public void setUnionWith(QueryDefinition unionWith) {
		this.unionWith = unionWith;
	}

	public void setRecursion(int recursion) {
		this.recursion = recursion;
	}

	public void union(QueryDefinition definition) {
		if (this.unionWith == null) {
			this.unionWith = definition;
		} else {
			this.unionWith.union(definition);
		}

	}

	public boolean HasSelected(String table, String column) {
		for (ISelectItem select : this.getSelect()) {
			if ((select instanceof SelectAll)
					|| (select instanceof SelectTableAll && ((SelectTableAll) select).getTable().equals(table))) {
				return true;
			}

			if (select instanceof SelectLiteral) {
				if (((SelectLiteral) select).hasAlias()) {
					String alias = ((SelectLiteral) select).getAlias();
					if (alias.equals(column)) {
						return true;
					}
				}
			} else if (select instanceof SelectItem) {
				SelectItem item = (SelectItem) select;
				if (item.getTable().equals(table) && (item.hasAlias() && item.getAlias().equals(column)
						|| !item.hasAlias() && item.getColumn().equals(column))) {
					return true;
				}
			}
		}

		return false;
	}

	public String toSql() {
		QueryBuilder qb2 = Database.getQueryBuilder();
		return qb2.toSql(this);
	}
}
