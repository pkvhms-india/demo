package com.varicent.common.query.sql.insert;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.varicent.common.query.domain.FullInsertDefinition;
import com.varicent.common.query.sql.QueryBuilder;
import com.varicent.core.database.portability.SqlSyntax;

public class FullInsertBuilder {
	private final SqlSyntax syntax;

	public FullInsertBuilder(SqlSyntax syntax) {
		this.syntax = syntax;
	}

	public String toSql(FullInsertDefinition insert) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(this.syntax.quote(insert.getTable()));
		sb.append(" ");
		if (!insert.getColumns().isEmpty()) {
			sb.append("(");
			List<String> columnSql = new ArrayList();

			for (String column : insert.getColumns()) {
				columnSql.add(this.syntax.quote(column));
			}

			sb.append(StringUtils.collectionToCommaDelimitedString(columnSql));
			sb.append(")");
		}

		QueryBuilder qb = new QueryBuilder(this.syntax);
		sb.append(qb.toSql(insert.getQuery()));
		return sb.toString();
	}
}
