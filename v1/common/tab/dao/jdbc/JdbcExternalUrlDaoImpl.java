package com.varicent.common.tab.dao.jdbc;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import com.varicent.common.query.domain.Constraint;
import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.tab.dao.ExternalUrlDao;
import com.varicent.core.database.Database;
import com.varicent.core.database.support.RowMapperUtil;

@Repository("externalUrlDao")
public class JdbcExternalUrlDaoImpl implements ExternalUrlDao {
	private final JdbcOperations operations;

	@Autowired
	public JdbcExternalUrlDaoImpl(DataSource ds) {
		this.operations = Database.getOperations(ds);
	}

	@Override
	public String getUrl(int tabId) {
		QueryDefinition def = new QueryDefinition("ExternalURL");
		def.select("ExternalURL", "URL");
		def.newConstraint("ExternalURL", "ID", Constraint.Operator.Equals, tabId);
		List<String> urls = this.operations.query(Database.getQueryBuilder().toSql(def),
				RowMapperUtil.getStringMapper());
		return !urls.isEmpty() ? (String) urls.get(0) : "";
	}
}
