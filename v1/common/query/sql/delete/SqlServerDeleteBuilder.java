package com.varicent.common.query.sql.delete;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.varicent.common.query.domain.DeleteDefinition;
import com.varicent.common.query.domain.Join;
import com.varicent.common.query.sql.SqlBuilder;
import com.varicent.core.database.Database;
import com.varicent.core.database.portability.SqlSyntax;

public class SqlServerDeleteBuilder implements DeleteBuilder {
	private final SqlSyntax syntax = Database.getSyntax();
	private final SqlBuilder builder;

	public SqlServerDeleteBuilder() {
		this.builder = new SqlBuilder(this.syntax);
	}

	@Override
	public String toSql(DeleteDefinition delete) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(this.syntax.quote(delete.getTable()));
		if (delete.getJoins().isEmpty()) {
			if (delete.getRestriction().hasConstraints()) {
				sb.append(" WHERE ");
				sb.append(this.builder.getSql(delete.getRestriction()));
			}

			return sb.toString();
		} else {
			sb.append(" FROM ");
			List<String> joinSql = new ArrayList();

			for (Join join : delete.getJoins()) {
				joinSql.add(this.builder.getSql(join.getSource()));
			}

			return StringUtils.collectionToCommaDelimitedString(joinSql);
		}
	}
}
