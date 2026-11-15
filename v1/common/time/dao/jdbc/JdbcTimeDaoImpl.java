package com.varicent.common.time.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.varicent.common.query.domain.Constraint;
import com.varicent.common.query.domain.Join;
import com.varicent.common.query.domain.OrClause;
import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.query.domain.SubQueryConstraint;
import com.varicent.common.query.sql.QueryBuilder;
import com.varicent.common.schema.dao.SchemaDao;
import com.varicent.common.time.dao.TimeDao;
import com.varicent.common.time.domain.Frequency;
import com.varicent.common.time.domain.ResetMonitor;
import com.varicent.common.time.domain.RevisionLockPeriod;
import com.varicent.common.time.domain.TimePeriod;
import com.varicent.core.database.Database;
import com.varicent.core.database.schema.ColumnDefinition;
import com.varicent.core.database.support.RowMapperUtil;

@Repository("timeDao")
public class JdbcTimeDaoImpl implements TimeDao {
	private final RowMapper<Frequency> frequencyMapper = new RowMapper<Frequency>() {
		@Override
		public Frequency mapRow(ResultSet rs, int rowNumber) throws SQLException {
			return new Frequency(rs.getString("TimeID"), rs.getString("LevelID"));
		}
	};
	private final RowMapper<TimePeriod> periodMapper = new RowMapper<TimePeriod>() {
		@Override
		public TimePeriod mapRow(ResultSet rs, int rowNumber) throws SQLException {
			String timeId = rs.getString("TimeID_");
			String name = rs.getString("Name_");
			String level = rs.getString("Level_");
			String parent = rs.getString("Parent_");
			Date starting = rs.getDate("Starting_");
			Date ending = rs.getDate("Ending_");
			return new TimePeriod(timeId, name, level, parent, starting, ending);
		}
	};
	private final RowMapper<RevisionLockPeriod> revisionLockPeriodMapper = new RowMapper<RevisionLockPeriod>() {
		@Override
		public RevisionLockPeriod mapRow(ResultSet rs, int rowNumber) throws SQLException {
			return new RevisionLockPeriod(JdbcTimeDaoImpl.this.periodMapper.mapRow(rs, rowNumber), -1);
		}
	};
	private final RowMapper<LockedPeriod> lockPeriodMapper = new RowMapper<LockedPeriod>() {
		@Override
		public LockedPeriod mapRow(ResultSet rs, int rowNumber) throws SQLException {
			return JdbcTimeDaoImpl.this.new LockedPeriod(rs.getString("Period"), rs.getInt("_Start"));
		}
	};
	private final RowMapper<CalendarLevel> calendarLevelMapper = new RowMapper<CalendarLevel>() {
		@Override
		public CalendarLevel mapRow(ResultSet rs, int rowNumber) throws SQLException {
			int order = rs.getInt("Order");
			String level = rs.getString("Level");
			return JdbcTimeDaoImpl.this.new CalendarLevel(order, level);
		}
	};
	private final JdbcOperations operations;

	@Autowired
	public JdbcTimeDaoImpl(DataSource ds) {
		this.operations = Database.getOperations(ds);
	}

	@Override
	public TimePeriod getFirstUnlockedPeriod(String calendar) {
		return this.getFirstUnlockedPeriod(calendar, this.getLowestLevel(calendar));
	}

	public TimePeriod getFirstUnlockedPeriod(String calendar, String level) {
		if (this.allPeriodsLocked(calendar, level)) {
			return null;
		} else {
			QueryDefinition query = new QueryDefinition("Time_");
			query.select("Time_", "Name_");
			query.newConstraint("Time_", "TimeID_", Constraint.Operator.Equals, calendar);
			query.newConstraint("Time_", "Level_", Constraint.Operator.Equals, level);
			QueryDefinition locked = new QueryDefinition("TimeLock");
			locked.selectAll();
			locked.newConstraint("Time_", "Name_", Constraint.Operator.Equals, "TimeLock", "Period");
			locked.newConstraint("Time_", "TimeID_", Constraint.Operator.Equals, "TimeLock", "TimeID");
			query.addConstraint(new SubQueryConstraint(SubQueryConstraint.Operator.NotExists, locked));
			query.limitTo(1);
			query.orderBy("Time_", "Starting_");
			QueryBuilder qb = Database.getQueryBuilder();
			String period = (String) this.operations.queryForObject(qb.toSql(query), String.class);
			return this.getTimePeriod(calendar, level, period);
		}
	}

	private boolean allPeriodsLocked(String calendar, String level) {
		QueryDefinition query = new QueryDefinition("Time_");
		query.selectCount();
		query.newConstraint("Time_", "TimeID_", Constraint.Operator.Equals, calendar);
		query.newConstraint("Time_", "Level_", Constraint.Operator.Equals, level);
		QueryDefinition locked = new QueryDefinition("TimeLock");
		locked.selectAll();
		locked.newConstraint("Time_", "Name_", Constraint.Operator.Equals, "TimeLock", "Period");
		locked.newConstraint("Time_", "TimeID_", Constraint.Operator.Equals, "TimeLock", "TimeID");
		query.addConstraint(new SubQueryConstraint(SubQueryConstraint.Operator.NotExists, locked));
		QueryBuilder qb = Database.getQueryBuilder();
		int count = (Integer) this.operations.queryForObject(qb.toSql(query), Integer.class);
		return count == 0;
	}

	@Override
	public Frequency getFrequency(String planId) {
		QueryDefinition query = new QueryDefinition("CompPlan");
		query.select("CompPlan", "CalendarUsed", "TimeID");
		query.select("CompPlan", "CalculationFrequency", "LevelID");
		query.newConstraint("CompPlan", "CompPlanID", Constraint.Operator.Equals, planId);
		QueryBuilder qb = Database.getQueryBuilder();
		List<Frequency> frequencies = this.operations.query(qb.toSql(query), this.frequencyMapper);
		return frequencies.isEmpty() ? null : (Frequency) frequencies.get(0);
	}

	@Override
	public Frequency getFrequency(String table, String column) {
		QueryDefinition query = new QueryDefinition("ReferenceTime");
		query.select("ReferenceTime", "TimeID");
		query.select("ReferenceTime", "LevelID");
		query.newConstraint("ReferenceTime", "TableName", Constraint.Operator.Equals, table);
		query.newConstraint("ReferenceTime", "ColumnName", Constraint.Operator.Equals, column);
		QueryBuilder qb = Database.getQueryBuilder();
		List<Frequency> frequencies = this.operations.query(qb.toSql(query), this.frequencyMapper);
		return frequencies.isEmpty() ? null : (Frequency) frequencies.get(0);
	}

	@Override
	public Date getSystemDate() {
		QueryDefinition query = new QueryDefinition();
		query.selectSystemDate("time");
		QueryBuilder qb = Database.getQueryBuilder();
		List<Date> todaysDate = this.operations.query(qb.toSql(query), RowMapperUtil.getDateMapper());
		return todaysDate.get(0);
	}

	@Override
	public ColumnDefinition getLockableDateColumn(String table, SchemaDao schemaDao) {
		QueryDefinition query = new QueryDefinition("CustomLockDate");
		query.select("CustomLockDate", "LockDate");
		query.newConstraint("CustomLockDate", "Table", Constraint.Operator.Equals, table);
		QueryBuilder qb = Database.getQueryBuilder();
		List<String> results = this.operations.query(qb.toSql(query), RowMapperUtil.getStringMapper());
		if (!results.isEmpty()) {
			String lockDateColumn = results.get(0);
			List<ColumnDefinition> columns = schemaDao.getColumns(table);
			int i = 0;

			for (int n = columns.size(); i < n; ++i) {
				ColumnDefinition column = columns.get(i);
				if (column.getName().equals(lockDateColumn)) {
					return column;
				}
			}
		}

		return null;
	}

	@Override
	public Date getLockedDate(String table) {
		String calendar = this.getCalendarForCustomTable(table);
		return calendar == null ? null : this.getLockedDate(calendar, this.getLowestLevel(calendar));
	}

	@Override
	public Date getLockedDate(String calendar, String level) {
		String periodName = this.getLockedPeriod(calendar);
		if (periodName == null) {
			return null;
		} else {
			TimePeriod locked = this.getTimePeriod(calendar, periodName);
			if (locked == null) {
				return null;
			} else {
				TimePeriod lockedPeriod = this.getPeriod(calendar, level, locked.getEnding());
				if (lockedPeriod == null) {
					return null;
				} else {
					if (lockedPeriod.getEnding().after(locked.getEnding())) {
						Date prevDate = DateUtils.addDays(lockedPeriod.getStarting(), -1);
						lockedPeriod = this.getPeriod(calendar, level, prevDate);
						if (lockedPeriod == null) {
							return null;
						}
					}

					return lockedPeriod.getEnding();
				}
			}
		}
	}

	@Override
	public List<TimePeriod> getLowestTimePeriods(String calendar) {
		return this.getTimePeriods(calendar, this.getLowestLevel(calendar), false);
	}

	@Override
	public ResetMonitor getResetMonitor(String calendar, String level, String resetLevel) {
		List<TimePeriod> levelPeriods = this.getTimePeriods(calendar, level, false);
		List<TimePeriod> resetLevelPeriods = this.getTimePeriods(calendar, resetLevel, false);
		return new ResetMonitor(levelPeriods, resetLevelPeriods);
	}

	@Override
	public TimePeriod getTimePeriod(String calendar, String level, String periodName) {
		QueryDefinition query = this.getPeriodQuery(calendar, level);
		query.newConstraint("Time_", "Name_", Constraint.Operator.Equals, periodName);
		QueryBuilder qb = Database.getQueryBuilder();
		List<TimePeriod> result = this.operations.query(qb.toSql(query), this.periodMapper);
		return result.isEmpty() ? null : (TimePeriod) result.get(0);
	}

	@Override
	public TimePeriod getTimePeriod(String calendar, String periodName) {
		return this.getTimePeriod(calendar, (String) null, periodName);
	}

	@Override
	public Map<String, List<TimePeriod>> getAllTimePeriods() {
		QueryDefinition query = new QueryDefinition("Time_");
		query.selectAll();
		QueryBuilder qb = Database.getQueryBuilder();
		List<TimePeriod> result = this.operations.query(qb.toSql(query), this.periodMapper);
		Map<String, List<TimePeriod>> calendars = new HashMap();

		for (TimePeriod period : result) {
			if (calendars.containsKey(period.getTimeId())) {
				calendars.get(period.getTimeId()).add(period);
			} else {
				List<TimePeriod> calendar = new ArrayList();
				calendar.add(period);
				calendars.put(period.getTimeId(), calendar);
			}
		}

		return calendars;
	}

	@Override
	public List<TimePeriod> getTimePeriods(String calendar, boolean levelDescending) {
		return this.getTimePeriods(calendar, (String) null, levelDescending);
	}

	@Override
	public List<TimePeriod> getTimePeriods(String calendar, String level, boolean levelDescending) {
		QueryDefinition query = this.getPeriodQuery(calendar, level, levelDescending);
		QueryBuilder qb = Database.getQueryBuilder();
		return this.operations.query(qb.toSql(query), this.periodMapper);
	}

	@Override
	public boolean isLockedRow(String table, List<Object> row, SchemaDao schemaDao) {
		ColumnDefinition lockableDateColumn = this.getLockableDateColumn(table, schemaDao);
		if (lockableDateColumn == null) {
			return false;
		} else {
			Date lastLockedDate = this.getLockedDate(table);
			if (lastLockedDate == null) {
				return false;
			} else {
				Date date = (Date) row.get(lockableDateColumn.getIndex());
				return date == null ? false : lastLockedDate.compareTo(date) >= 0;
			}
		}
	}

	private String getLockedPeriod(String calendar) {
		QueryDefinition query = new QueryDefinition("TimeCalendar");
		query.select("TimeCalendar", "LastLockedPeriod");
		query.newConstraint("TimeCalendar", "TimeID", Constraint.Operator.Equals, calendar);
		QueryBuilder qb = Database.getQueryBuilder();
		List<String> result = this.operations.query(qb.toSql(query), RowMapperUtil.getStringMapper());
		return result.isEmpty() ? null : (String) result.get(0);
	}

	private String getLowestLevel(String calendar) {
		QueryDefinition query = new QueryDefinition("TimeLevel");
		query.limitTo(1);
		query.select("TimeLevel", "Level");
		query.newConstraint("TimeLevel", "TimeID", Constraint.Operator.Equals, calendar);
		query.orderBy("TimeLevel", "Order", true);
		QueryBuilder qb = Database.getQueryBuilder();
		List<String> result = this.operations.query(qb.toSql(query), RowMapperUtil.getStringMapper());
		return result.isEmpty() ? null : (String) result.get(0);
	}

	private TimePeriod getPeriod(String calendar, String level, Date date) {
		QueryDefinition query = this.getPeriodQuery(calendar, level);
		query.newConstraint("Time_", "Starting_", Constraint.Operator.LessThanEquals, date);
		query.newConstraint("Time_", "Ending_", Constraint.Operator.GreaterThanEquals, date);
		QueryBuilder qb = Database.getQueryBuilder();
		List<TimePeriod> result = this.operations.query(qb.toSql(query), this.periodMapper);
		return result.isEmpty() ? null : (TimePeriod) result.get(0);
	}

	private QueryDefinition getPeriodQuery(String calendar, String level) {
		return this.getPeriodQuery(calendar, level, false);
	}

	private QueryDefinition getPeriodQuery(String calendar, String level, boolean levelDescending, Date start,
			Date end) {
		QueryDefinition query = new QueryDefinition("Time_");
		query.selectAll();
		Join join = new Join("TimeLevel", Join.Type.INNER);
		join.newConstraint("Time_", "TimeID_", Constraint.Operator.Equals, "TimeLevel", "TimeID");
		join.newConstraint("Time_", "Level_", Constraint.Operator.Equals, "TimeLevel", "Level");
		query.joinWith(join);
		query.newConstraint("Time_", "TimeID_", Constraint.Operator.Equals, calendar);
		if (start != null) {
			query.newConstraint("Time_", "Starting_", Constraint.Operator.GreaterThanEquals, start);
		}

		if (end != null) {
			query.newConstraint("Time_", "Ending_", Constraint.Operator.LessThanEquals, end);
		}

		if (level != null) {
			query.newConstraint("Time_", "Level_", Constraint.Operator.Equals, level);
		}

		query.newNullConstraint("Time_", "Parent_", false);
		query.orderBy("TimeLevel", "Order", levelDescending);
		query.orderBy("Time_", "Starting_");
		return query;
	}

	@Override
	public List<TimePeriod> getTimePeriods(String calendar, Date start, Date end) {
		QueryDefinition query = this.getPeriodQuery(calendar, (String) null, false, start, end);
		QueryBuilder qb = Database.getQueryBuilder();
		return this.operations.query(qb.toSql(query), this.periodMapper);
	}

	private QueryDefinition getPeriodQuery(String calendar, String level, boolean levelDescending) {
		return this.getPeriodQuery(calendar, level, levelDescending, (Date) null, (Date) null);
	}

	public List<RevisionLockPeriod> getRevisionLockPeriods(String calendar) {
		String timeLockHistory = "_HistoryTimeLock";
		QueryDefinition query = new QueryDefinition(timeLockHistory);
		query.select(timeLockHistory, "_Start");
		query.select("Time_", "TimeID_");
		query.select("Time_", "Name_");
		query.select("Time_", "Level_");
		query.select("Time_", "Parent_");
		query.select("Time_", "Starting_");
		query.select("Time_", "Ending_");
		query.newConstraint("Time_", "TimeID_", Constraint.Operator.Equals, calendar);
		query.newNullConstraint(timeLockHistory, "_End", true);
		Join join = new Join("Time_", Join.Type.INNER);
		join.newConstraint(timeLockHistory, "TimeID_", Constraint.Operator.Equals, "Time_", "TimeID_");
		join.newConstraint(timeLockHistory, "Period", Constraint.Operator.Equals, "Time_", "Name_");
		query.joinWith(join);
		query.orderBy("Time_", "Ending_");
		QueryBuilder qb = Database.getQueryBuilder();
		return this.operations.query(qb.toSql(query), this.revisionLockPeriodMapper);
	}

	private int getLatestRevision() {
		QueryDefinition query = new QueryDefinition("HistoryRevision");
		query.selectMax("HistoryRevision", "RevisionID", "RevisionID");
		QueryBuilder qb = Database.getQueryBuilder();
		return (Integer) this.operations.queryForObject(qb.toSql(query), Integer.class);
	}

	@Override
	public List<RevisionLockPeriod> getRevisionLockPeriods(String calendar, String level) {
		int revision = this.getLatestRevision();
		List<RevisionLockPeriod> periods = this.getTimePeriods(calendar, revision);
		List<RevisionLockPeriod> revisions = new ArrayList();

		for (RevisionLockPeriod rlp : periods) {
			TimePeriod tp = rlp.getPeriod();
			if (tp.getLevel().equals(level) && this.getPeriodChildren(tp.getName(), calendar) == this
					.getPeriodChildren(tp.getName(), periods)) {
				revisions.add(rlp);
			}
		}

		return revisions;
	}

	int getPeriodChildren(String period, String calendar) {
		QueryDefinition query = new QueryDefinition("Time_");
		query.selectCount();
		query.newConstraint("Time_", "TimeID_", Constraint.Operator.Equals, calendar);
		query.newConstraint("Time_", "Parent_", Constraint.Operator.Equals, period);
		QueryBuilder qb = Database.getQueryBuilder();
		return (Integer) this.operations.queryForObject(qb.toSql(query), Integer.class);
	}

	int getPeriodChildren(String period, List<RevisionLockPeriod> periods) {
		int count = 0;

		for (RevisionLockPeriod rlp : periods) {
			TimePeriod tp = rlp.getPeriod();
			if (tp.getParent() != null && tp.getParent().equals(period)) {
				++count;
			}
		}

		return count;
	}

	List<RevisionLockPeriod> getTimePeriods(String calendar, int revision) {
		List<RevisionLockPeriod> periods = this.getPeriods(calendar, revision);
		List<LockedPeriod> locked = this.getLockedPeriods(calendar, revision);
		Hashtable<Integer, String> levels = this.getTimeLevels(calendar, revision);
		String lowestLevel = levels.get(levels.keySet().size() - 1);

		for (LockedPeriod lock : locked) {
			this.setPeriodRevision(lock.getPeriod(), lowestLevel, lock.getRevision(), periods, levels);
		}

		for (int i = periods.size() - 1; i >= 0; --i) {
			RevisionLockPeriod period = periods.get(i);
			if (period.getRevision() == -1) {
				periods.remove(period);
			}
		}

		return periods;
	}

	List<RevisionLockPeriod> getPeriods(String calendar, int revision) {
		String timeHistory = "_HistoryTime_";
		QueryDefinition query = new QueryDefinition(timeHistory);
		query.select(timeHistory, "_Start");
		query.select(timeHistory, "TimeID_");
		query.select(timeHistory, "Name_");
		query.select(timeHistory, "Level_");
		query.select(timeHistory, "Parent_");
		query.select(timeHistory, "Starting_");
		query.select(timeHistory, "Ending_");
		query.newConstraint(timeHistory, "TimeID_", Constraint.Operator.Equals, calendar);
		query.newNullConstraint(timeHistory, "_End", true);
		Join join = new Join("Time_", Join.Type.INNER);
		join.newConstraint(timeHistory, "TimeID_", Constraint.Operator.Equals, "Time_", "TimeID_");
		join.newConstraint(timeHistory, "Name_", Constraint.Operator.Equals, "Time_", "Name_");
		join.newConstraint(timeHistory, "Level_", Constraint.Operator.Equals, "Time_", "Level_");
		query.joinWith(join);
		query.orderBy("Time_", "Ending_");
		QueryBuilder qb = Database.getQueryBuilder();
		List<RevisionLockPeriod> periods = this.operations.query(qb.toSql(query), this.revisionLockPeriodMapper);
		return periods;
	}

	List<LockedPeriod> getLockedPeriods(String calendar, int revision) {
		String timeLockHistory = "_HistoryTimeLock";
		QueryDefinition query = new QueryDefinition(timeLockHistory);
		query.select(timeLockHistory, "Period");
		query.select(timeLockHistory, "_Start");
		query.newConstraint(timeLockHistory, "_Start", Constraint.Operator.LessThanEquals, revision);
		OrClause clause = new OrClause();
		clause.newConstraint(timeLockHistory, "_End", Constraint.Operator.GreaterThan, revision);
		clause.newNullConstraint(timeLockHistory, "_End", true);
		query.addConstraint(clause);
		query.newConstraint(timeLockHistory, "TimeID", Constraint.Operator.Equals, calendar);
		QueryBuilder qb = Database.getQueryBuilder();
		List<LockedPeriod> locked = this.operations.query(qb.toSql(query), this.lockPeriodMapper);
		return locked;
	}

	Hashtable<Integer, String> getTimeLevels(String calendar, int revision) {
		String timeLevelHistory = "_HistoryTimeLevel";
		QueryDefinition query = new QueryDefinition(timeLevelHistory);
		query.select(timeLevelHistory, "Order");
		query.select(timeLevelHistory, "Level");
		query.newConstraint(timeLevelHistory, "_Start", Constraint.Operator.LessThanEquals, revision);
		OrClause clause = new OrClause();
		clause.newConstraint(timeLevelHistory, "_End", Constraint.Operator.GreaterThan, revision);
		clause.newNullConstraint(timeLevelHistory, "_End", true);
		query.addConstraint(clause);
		query.newConstraint(timeLevelHistory, "TimeID", Constraint.Operator.Equals, calendar);
		QueryBuilder qb = Database.getQueryBuilder();
		List<CalendarLevel> levels = this.operations.query(qb.toSql(query), this.calendarLevelMapper);
		Hashtable<Integer, String> levelsDic = new Hashtable();

		for (CalendarLevel level : levels) {
			levelsDic.put(new Integer(level.getOrder()), level.getLevel());
		}

		return levelsDic;
	}

	void setPeriodRevision(String period, String level, int revision, List<RevisionLockPeriod> periods,
			Hashtable<Integer, String> timeLevels) {
		for (RevisionLockPeriod rlp : periods) {
			TimePeriod tp = rlp.getPeriod();
			if (tp.getName().equals(period) && tp.getLevel().equals(level)) {
				rlp.setRevision(Math.max(rlp.getRevision(), revision));
				String parentLevel = null;

				for (Integer key : timeLevels.keySet()) {
					if (timeLevels.get(key) == level) {
						parentLevel = timeLevels.get(key - 1);
						break;
					}
				}

				if (tp.getParent() != null) {
					this.setPeriodRevision(tp.getParent(), parentLevel, rlp.getRevision(), periods, timeLevels);
				}
			}
		}

	}

	@Override
	public boolean isDateValidForTable(Date date, String table) {
		if (date == null) {
			return false;
		} else {
			String calendar = this.getCalendarForCustomTable(table);
			if (calendar == null) {
				return false;
			} else {
				TimePeriod firstUnlockPeriod = this.getFirstUnlockedPeriod(calendar);
				if (firstUnlockPeriod == null) {
					return false;
				} else if (date.before(firstUnlockPeriod.getStarting())) {
					return false;
				} else {
					QueryDefinition query = new QueryDefinition("Time_");
					query.select("Time_", "TimeID_");
					query.newConstraint("Time_", "TimeID_", Constraint.Operator.Equals, calendar);
					query.newNullConstraint("Time_", "Parent_", true);
					query.newConstraint("Time_", "Ending_", Constraint.Operator.LessThan, date);
					QueryBuilder qb = Database.getQueryBuilder();
					List<String> results = this.operations.query(qb.toSql(query), RowMapperUtil.getStringMapper());
					return results.isEmpty();
				}
			}
		}
	}

	private String getCalendarForCustomTable(String table) {
		QueryDefinition query = new QueryDefinition("CustomTableTime");
		query.select("CustomTableTime", "TimeID");
		query.newConstraint("CustomTableTime", "TableName", Constraint.Operator.Equals, table);
		QueryBuilder qb = Database.getQueryBuilder();
		List<String> results = this.operations.query(qb.toSql(query), RowMapperUtil.getStringMapper());
		return results.isEmpty() ? null : (String) results.get(0);
	}

	@Override
	public boolean isTableCustom(String table) {
		QueryDefinition query = new QueryDefinition("CustomTable");
		query.select("CustomTable", "TableType");
		query.newConstraint("CustomTable", "TableName", Constraint.Operator.Equals, table);
		QueryBuilder qb = Database.getQueryBuilder();
		List<String> result = this.operations.query(qb.toSql(query), RowMapperUtil.getStringMapper());
		return result.size() > 0 && (result.get(0).equals("Custom") || result.get(0).equals("Structural"));
	}

	class LockedPeriod {
		private String period;
		private int revision;

		public LockedPeriod(String period, int revision) {
			this.period = period;
			this.revision = revision;
		}

		public String getPeriod() {
			return this.period;
		}

		public int getRevision() {
			return this.revision;
		}
	}

	class CalendarLevel {
		private int order;
		private String level;

		public CalendarLevel(int order, String level) {
			this.order = order;
			this.level = level;
		}

		public int getOrder() {
			return this.order;
		}

		public String getLevel() {
			return this.level;
		}
	}
}
