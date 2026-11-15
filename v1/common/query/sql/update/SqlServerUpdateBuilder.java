package com.varicent.common.query.sql.update;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.varicent.common.query.domain.DataField;
import com.varicent.common.query.domain.SqlDataSource;
import com.varicent.common.query.domain.UpdateDefinition;
import com.varicent.common.query.domain.UpdateSourceItem;
import com.varicent.common.query.sql.SqlBuilder;
import com.varicent.core.database.Database;
import com.varicent.core.database.portability.SqlSyntax;

public class SqlServerUpdateBuilder extends AbstractUpdateBuilder {
	private final SqlSyntax syntax = Database.getSyntax();
	private final SqlBuilder builder;

	public SqlServerUpdateBuilder() {
		this.builder = new SqlBuilder(this.syntax);
	}

	@Override
	public String toSql(UpdateDefinition update) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ");
		sb.append(this.syntax.quote(update.getTable()));
		sb.append(" SET ");
		List<String> setSql = new ArrayList();
		int sourceIndex = 0;

		for (DataField field : update.getFields()) {
			StringBuilder fieldSql = new StringBuilder();
			fieldSql.append(this.builder.getSql(field));
			fieldSql.append(" = ");
			UpdateSourceItem source = update.getSources().get(sourceIndex);
			switch (source.getType()) {
			case FIELD:
				fieldSql.append(this.builder.getSql(source.getField()));
				break;
			default:
				Assert.isTrue(source.getType().equals(UpdateSourceItem.SourceType.VALUE));
				fieldSql.append(this.builder.getSql(source.getLiteral()));
			}

			setSql.add(fieldSql.toString());
			++sourceIndex;
		}

		sb.append(StringUtils.collectionToCommaDelimitedString(setSql));
		if (!update.getCrossJoins().isEmpty()) {
			sb.append(" FROM ");
			List<String> joinSql = new ArrayList();

			for (SqlDataSource source : update.getCrossJoins()) {
				joinSql.add(this.builder.getSql(source));
			}

			sb.append(StringUtils.collectionToCommaDelimitedString(joinSql));
		}

		if (update.getRestriction().hasConstraints()) {
			sb.append(" WHERE ");
			sb.append(this.builder.getSql(update.getRestriction()));
		}

		return sb.toString();
	}

	@Override
	protected boolean isSourceName(UpdateDefinition update, String name) {
		return false;
	}
}
