package com.varicent.common.adjustment.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.varicent.common.adjustment.dao.AdjustmentDao;
import com.varicent.common.adjustment.domain.Adjustment;
import com.varicent.common.query.domain.Constraint;
import com.varicent.common.query.domain.InsertDefinition;
import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.query.sql.QueryBuilder;
import com.varicent.common.query.sql.insert.InsertBuilder;
import com.varicent.common.time.domain.TimePeriod;
import com.varicent.core.database.Database;
import com.varicent.security.domain.UserAccount;

@Repository("adjustmentDao")
public class JdbcAdjustmentDaoImpl implements AdjustmentDao {
	private final RowMapper<Adjustment> autoAdjustmentMapper = new RowMapper<Adjustment>() {
		@Override
		public Adjustment mapRow(ResultSet rs, int rowNumber) throws SQLException {
			String comment = rs.getString("Comment");
			return new Adjustment(rs.getInt("CorrectionID"), rs.getString("CompPlanID"), rs.getString("PayeeID"),
					rs.getDate("Date"), rs.getBigDecimal("Value"), rs.getString("CreatedBy"),
					rs.getTimestamp("CreatedAt"), Database.getSyntax().isEmptyString(comment) ? "" : comment, false);
		}
	};
	private final RowMapper<Adjustment> manualAdjustmentMapper = new RowMapper<Adjustment>() {
		@Override
		public Adjustment mapRow(ResultSet rs, int rowNumber) throws SQLException {
			String comment = rs.getString("Comment");
			return new Adjustment(rs.getInt("CorrectionID"), rs.getString("CompPlanID"), rs.getString("PayeeID"),
					rs.getDate("Date"), rs.getBigDecimal("Value"), rs.getString("CreatedBy"),
					rs.getTimestamp("CreatedAt"), Database.getSyntax().isEmptyString(comment) ? "" : comment, true);
		}
	};
	private final JdbcOperations operations;

	@Autowired
	public JdbcAdjustmentDaoImpl(DataSource ds) {
		this.operations = Database.getOperations(ds);
	}

	@Override
	public void addManualAdjustment(Adjustment adjustment) {
		Assert.isTrue(adjustment.isManual());
		InsertDefinition insert = new InsertDefinition("CorrectionManual",
				new String[] { "CompPlanID", "PayeeID", "Date", "Value", "CreatedBy", "CreatedAt", "Comment" });
		insert.addValue(adjustment.getPlanId());
		insert.addValue(adjustment.getPayeeId());
		insert.addValue(adjustment.getDate());
		insert.addValue(adjustment.getValue());
		insert.addValue(UserAccount.getCurrent().getPayeeId());
		insert.addSystemDateTime();
		insert.addValue(adjustment.getComment());
		InsertBuilder ib = Database.getInsertBuilder();
		this.operations.update(ib.toSql(insert));
	}

	public List<Adjustment> getAutoAdjustments(String planId, String payeeId, TimePeriod period) {
		QueryDefinition query = this.getAdjustmentQuery("CorrectionAuto", planId, payeeId, period);
		QueryBuilder qb = Database.getQueryBuilder();
		return this.operations.query(qb.toSql(query), this.autoAdjustmentMapper);
	}

	@Override
	public List<Adjustment> getManualAdjustments(String planId, String payeeId, TimePeriod period) {
		QueryDefinition query = this.getAdjustmentQuery("CorrectionManual", planId, payeeId, period);
		QueryBuilder qb = Database.getQueryBuilder();
		return this.operations.query(qb.toSql(query), this.manualAdjustmentMapper);
	}

	private QueryDefinition getAdjustmentQuery(String table, String planId, String payeeId, TimePeriod period) {
		QueryDefinition query = new QueryDefinition(table);
		query.selectAll();
		query.newConstraint(table, "CompPlanID", Constraint.Operator.Equals, planId);
		query.newConstraint(table, "PayeeID", Constraint.Operator.Equals, payeeId);
		query.newConstraint(table, "Date", Constraint.Operator.GreaterThanEquals, period.getStarting());
		query.newConstraint(table, "Date", Constraint.Operator.LessThanEquals, period.getEnding());
		return query;
	}
}
