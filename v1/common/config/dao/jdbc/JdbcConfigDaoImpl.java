package com.varicent.common.config.dao.jdbc;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import com.varicent.common.config.dao.ConfigDao;
import com.varicent.common.query.domain.CustomConstraint;
import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.query.sql.QueryBuilder;
import com.varicent.core.database.Database;
import com.varicent.core.database.portability.SqlSyntax;
import com.varicent.core.database.support.RowMapperUtil;

@Repository("configDao")
public class JdbcConfigDaoImpl implements ConfigDao {
	private final JdbcOperations operations;

	@Autowired
	public JdbcConfigDaoImpl(DataSource ds) {
		this.operations = Database.getOperations(ds);
	}

	@Override
	public Boolean getBooleanValue(ConfigDao.Key key) {
		String value = this.getValue(key);
		return value == null ? null : Boolean.valueOf(value);
	}

	@Override
	public Integer getIntValue(ConfigDao.Key key) {
		String value = this.getValue(key);
		return value == null ? null : Integer.valueOf(value);
	}

	@Override
	public String getValue(ConfigDao.Key key) {
		QueryDefinition query = new QueryDefinition("Config");
		query.select("Config", "Value");
		SqlSyntax syntax = Database.getSyntax();
		String sql = String.format("UPPER(%s) = %s", syntax.quote("Key"), syntax.cast(key.toString().toUpperCase()));
		query.addConstraint(new CustomConstraint(sql));
		QueryBuilder qb = Database.getQueryBuilder();
		List<String> result = this.operations.query(qb.toSql(query), RowMapperUtil.getStringMapper());
		return result.isEmpty() ? null : (String) result.get(0);
	}

	@Override
	public boolean isModelUnicode() {
		return this.operations.query(Database.getSyntax().getUnicodeSQL(), RowMapperUtil.getStringMapper()).size() > 0;
	}
}
