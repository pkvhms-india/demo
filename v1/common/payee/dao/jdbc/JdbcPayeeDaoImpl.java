package com.varicent.common.payee.dao.jdbc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import com.varicent.common.payee.dao.PayeeDao;
import com.varicent.common.payee.domain.Payee;
import com.varicent.common.query.domain.Constraint;
import com.varicent.common.query.domain.CustomConstraint;
import com.varicent.common.query.domain.Join;
import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.query.sql.QueryBuilder;
import com.varicent.core.database.Database;
import com.varicent.core.database.portability.SqlSyntax;
import com.varicent.core.database.support.RowMapperUtil;
import com.varicent.security.domain.UserKey;

@Repository("payeeDao")
public class JdbcPayeeDaoImpl implements PayeeDao {
	private final JdbcOperations operations;

	@Autowired
	public JdbcPayeeDaoImpl(DataSource ds) {
		this.operations = Database.getOperations(ds);
	}

	@Override
	public Payee getPayee(String payeeId) {
		QueryDefinition query = new QueryDefinition("Payee_");
		query.select("Payee_", "PayeeID_");
		query.select("Payee_", "Name_");
		query.select("Payee_", "Email_");
		query.newConstraint("Payee_", "PayeeID_", Constraint.Operator.Equals, payeeId);
		QueryBuilder qb = Database.getQueryBuilder();
		List<Payee> payees = this.operations.query(qb.toSql(query), RowMapperUtil.getPayeeMapper());
		return payees.isEmpty() ? null : (Payee) payees.get(0);
	}

	@Override
	public Payee getPayeeByKey(String key) {
		SqlSyntax syntax = Database.getSyntax();
		String userKey = syntax.cast(key.toLowerCase());
		String userKeyColumn = UserKey.getColumn();
		QueryDefinition query = new QueryDefinition("Payee_");
		query.select("Payee_", "PayeeID_");
		query.select("Payee_", "Name_");
		query.select("Payee_", "Email_");
		String sqlServerConstraint = String.format("LOWER([Payee_].[%s]) = %s", userKeyColumn, userKey);
		query.addConstraint(new CustomConstraint(sqlServerConstraint));
		QueryBuilder qb = Database.getQueryBuilder();
		List<Payee> payees = this.operations.query(qb.toSql(query), RowMapperUtil.getPayeeMapper());
		return payees.isEmpty() ? null : (Payee) payees.get(0);
	}

	@Override
	public Payee getPayeeByEmail(String email) {
		SqlSyntax syntax = Database.getSyntax();
		String userEmail = syntax.cast(email.toLowerCase());
		String userEmailColumn = "Email_";
		QueryDefinition query = new QueryDefinition("Payee_");
		query.select("Payee_", "PayeeID_");
		query.select("Payee_", "Name_");
		query.select("Payee_", "Email_");
		Join userJoin = new Join("Users", Join.Type.INNER);
		userJoin.newConstraint("Users", "PayeeID", Constraint.Operator.Equals, "Payee_", "PayeeID_");
		query.joinWith(userJoin);
		String sqlServerConstraint = String.format("LOWER([Payee_].[%s]) = %s", userEmailColumn, userEmail);
		query.addConstraint(new CustomConstraint(sqlServerConstraint));
		QueryBuilder qb = Database.getQueryBuilder();
		List<Payee> payees = this.operations.query(qb.toSql(query), RowMapperUtil.getPayeeMapper());
		return payees.isEmpty() ? null : (Payee) payees.get(0);
	}

	@Override
	public Set<String> getPayeeGroupIds(String payeeId) {
		QueryDefinition query = PayeeQuery.getPayeeGroupQuery(payeeId);
		query.select("GroupMember_", "PayeeGroupID_");
		QueryBuilder qb = Database.getQueryBuilder();
		List<String> payeeGroupIds = this.operations.query(qb.toSql(query), RowMapperUtil.getStringMapper());
		return new HashSet(payeeGroupIds);
	}
}
