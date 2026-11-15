package com.varicent.common.calculation.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.varicent.common.calculation.dao.CalculationDao;
import com.varicent.common.calculation.domain.Calculation;
import com.varicent.common.query.domain.Constraint;
import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.query.sql.QueryBuilder;
import com.varicent.core.database.Database;

@Repository("calculationDao")
public class JdbcCalculationDaoImpl implements CalculationDao {
	private final RowMapper<Calculation> calcMapper = new RowMapper<Calculation>() {
		@Override
		public Calculation mapRow(ResultSet rs, int rowNumber) throws SQLException {
			return new Calculation(rs.getInt("CalculationID"), rs.getInt("Type"), rs.getString("Name"),
					rs.getString("Comment"), rs.getString("CalendarUsed"), rs.getString("TimeLevel"),
					rs.getString("ResetLevel"), rs.getDate("TimeStart"), rs.getDate("TimeEnd"));
		}
	};
	private final JdbcOperations operations;

	@Autowired
	public JdbcCalculationDaoImpl(DataSource ds) {
		this.operations = Database.getOperations(ds);
	}

	@Override
	public Calculation getCalculation(int calcId) {
		QueryDefinition query = new QueryDefinition("Calculation");
		query.selectAll();
		query.newConstraint("Calculation", "CalculationID", Constraint.Operator.Equals, calcId);
		QueryBuilder qb = Database.getQueryBuilder();
		List<Calculation> result = this.operations.query(qb.toSql(query), this.calcMapper);
		if (result.isEmpty()) {
			return null;
		} else {
			Calculation calc = result.get(0);
			return this.loadCalculation(calc);
		}
	}

	private Calculation loadCalculation(Calculation calc) {
		QueryDefinition query = null;
		QueryBuilder qb = null;
		SqlRowSet result = null;
		switch (calc.getType()) {
		case 2:
			query = new QueryDefinition("TimeShift");
			query.selectAll();
			query.newConstraint("TimeShift", "CalculationID", Constraint.Operator.Equals, calc.getId());
			qb = Database.getQueryBuilder();
			result = this.operations.queryForRowSet(qb.toSql(query));
			if (result.next()) {
				calc.setTimeLockDetails(result.getString("TimeID"), result.getString("LevelID"), (Date) null,
						(Date) null);
			}
			break;
		case 4:
			query = new QueryDefinition("CalculationCategoryTime");
			query.selectAll();
			query.newConstraint("CalculationCategoryTime", "CalculationID", Constraint.Operator.Equals, calc.getId());
			qb = Database.getQueryBuilder();
			result = this.operations.queryForRowSet(qb.toSql(query));
			if (result.next()) {
				calc.setTimeLockDetails(result.getString("CalendarUsed"), result.getString("TimeLevel"),
						result.getDate("TimeStart"), result.getDate("TimeEnd"));
			}
		}

		return calc;
	}

	@Override
	public int getResettingPartitionCount(int calcId) {
		QueryDefinition calcQuery = new QueryDefinition("Calculation");
		calcQuery.select("Calculation", "Type");
		calcQuery.newConstraint("Calculation", "CalculationID", Constraint.Operator.Equals, calcId);
		QueryBuilder calcQb = Database.getQueryBuilder();
		int calcType = (Integer) this.operations.queryForObject(calcQb.toSql(calcQuery), Integer.class);
		if (calcType == 2) {
			return 2;
		} else {
			QueryDefinition query = new QueryDefinition("CalculationPartition");
			query.selectCount();
			query.newConstraint("CalculationPartition", "CalculationID", Constraint.Operator.Equals, calcId);
			query.newConstraint("CalculationPartition", "Accumulating", Constraint.Operator.Equals, 0);
			QueryBuilder qb = Database.getQueryBuilder();
			int resettingPartitionCount = (Integer) this.operations.queryForObject(qb.toSql(query), Integer.class);
			if (calcType != 1) {
				return resettingPartitionCount;
			} else {
				--resettingPartitionCount;
				return resettingPartitionCount;
			}
		}
	}
}
