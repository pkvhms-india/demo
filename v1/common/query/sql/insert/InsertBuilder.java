package com.varicent.common.query.sql.insert;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.varicent.common.query.domain.InsertDefinition;
import com.varicent.common.query.domain.Literal;
import com.varicent.common.query.sql.SqlBuilder;
import com.varicent.core.database.portability.SqlSyntax;

public class InsertBuilder {
	private final SqlSyntax syntax;
	private final SqlBuilder builder;

	public InsertBuilder(SqlSyntax syntax) {
		this.syntax = syntax;
		this.builder = new SqlBuilder(syntax);
	}

	public String toSql(InsertDefinition insert) {
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

		sb.append(" VALUES (");
		List<String> valueSql = new ArrayList();

		for (Literal literal : insert.getValues()) {
			valueSql.add(this.builder.getSql(literal));
		}

		sb.append(StringUtils.collectionToCommaDelimitedString(valueSql));
		sb.append(")");
		return sb.toString();
	}
}
