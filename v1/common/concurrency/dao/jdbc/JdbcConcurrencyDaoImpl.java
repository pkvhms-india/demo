package com.varicent.common.concurrency.dao.jdbc;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import com.varicent.common.concurrency.dao.ConcurrencyDao;
import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.query.sql.QueryBuilder;
import com.varicent.core.database.Database;

@Repository("concurrencyDao")
public class JdbcConcurrencyDaoImpl implements ConcurrencyDao {
	private final JdbcOperations operations;

	@Autowired
	public JdbcConcurrencyDaoImpl(DataSource ds) {
		this.operations = Database.getOperations(ds);
	}

	@Override
	public boolean isGlobalLocked() {
		QueryDefinition query = new QueryDefinition("Token");
		query.selectCount();
		query.newNullConstraint("Token", "SessionID", false);
		QueryBuilder qb = Database.getQueryBuilder();
		return this.operations.queryForObject(qb.toSql(query), Integer.class) > 0;
	}

	@Override
	public boolean BufferTableEmpty(String bufferTable) {
		QueryDefinition qd = new QueryDefinition(bufferTable);
		qd.selectCount();
		return this.operations.queryForObject(qd.toSql(), Integer.class) == 0;
	}
}
