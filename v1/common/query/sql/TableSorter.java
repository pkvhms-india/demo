package com.varicent.common.query.sql;

import com.varicent.common.query.domain.Constraint;
import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.time.dao.TimeDao;
import com.varicent.common.time.domain.Frequency;

public class TableSorter {
	private static final String ORDER_BY_PREFIX = "_OrderByPrefix";
	private final TimeDao timeDao;
	private int index;

	public TableSorter(TimeDao timeDao) {
		this.timeDao = timeDao;
	}

	public void orderBy(QueryDefinition query, String table, String column) {
		this.orderBy(query, table, column, table);
	}

	public void orderBy(QueryDefinition query, String table, String column, String alias) {
		this.orderBy(query, table, column, alias, false);
	}

	public void orderBy(QueryDefinition query, String table, String column, String alias, boolean descending) {
		Frequency freq = this.timeDao.getFrequency(table, column);
		if (freq != null) {
			this.orderByTime(query, alias, column, freq, descending);
		} else {
			query.orderBy(alias, column, descending);
		}

	}

	public void orderByTime(QueryDefinition query, String alias, String column, Frequency freq) {
		this.orderByTime(query, alias, column, freq, false);
	}

	public void orderByTime(QueryDefinition query, String alias, String column, Frequency freq, boolean descending) {
		String timeAlias = this.getTimeAlias();
		query.crossJoin("Time_", timeAlias);
		query.newConstraint(timeAlias, "TimeID_", Constraint.Operator.Equals, freq.getTimeId());
		query.newConstraint(timeAlias, "Level_", Constraint.Operator.Equals, freq.getLevelId());
		query.newConstraint(timeAlias, "Name_", Constraint.Operator.Equals, alias, column);
		query.orderBy(timeAlias, "Ending_", descending);
	}

	private String getTimeAlias() {
		return "_OrderByPrefix" + this.index++;
	}
}
