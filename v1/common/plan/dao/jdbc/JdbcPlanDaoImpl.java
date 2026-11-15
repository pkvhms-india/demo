package com.varicent.common.plan.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.varicent.common.plan.dao.PlanDao;
import com.varicent.common.plan.domain.Plan;
import com.varicent.common.query.domain.Constraint;
import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.query.sql.QueryBuilder;
import com.varicent.core.database.Database;

@Repository("planDao")
public class JdbcPlanDaoImpl implements PlanDao {
	private final RowMapper<Plan> planMapper = new RowMapper<Plan>() {
		@Override
		public Plan mapRow(ResultSet rs, int rowNumber) throws SQLException {
			return new Plan(rs.getString("CompPlanID"), rs.getString("Name"), rs.getString("CalendarUsed"));
		}
	};
	private final JdbcOperations operations;

	@Autowired
	public JdbcPlanDaoImpl(DataSource ds) {
		this.operations = Database.getOperations(ds);
	}

	@Override
	public Plan getPlan(String planId) {
		QueryDefinition query = new QueryDefinition("CompPlan");
		query.selectAll();
		query.newConstraint("CompPlan", "CompPlanID", Constraint.Operator.Equals, planId);
		QueryBuilder qb = Database.getQueryBuilder();
		List<Plan> plans = this.operations.query(qb.toSql(query), this.planMapper);
		return plans.isEmpty() ? null : (Plan) plans.get(0);
	}
}
