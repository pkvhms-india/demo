package com.varicent.common.reference.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.varicent.common.history.dao.HistoryDao;
import com.varicent.common.query.domain.Constraint;
import com.varicent.common.query.domain.DataField;
import com.varicent.common.query.domain.Join;
import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.query.sql.QueryBuilder;
import com.varicent.common.reference.dao.ReferenceDao;
import com.varicent.common.reference.domain.Reference;
import com.varicent.common.schema.dao.SchemaDao;
import com.varicent.core.database.Database;

@Repository("referenceDao")
public class JdbcReferenceDaoImpl implements ReferenceDao {
	private final RowMapper<Reference> referenceMapper = new RowMapper<Reference>() {
		@Override
		public Reference mapRow(ResultSet rs, int rowNumber) throws SQLException {
			return new Reference(rs.getString("TableName"), rs.getString("ColumnName"), rs.getString("ReferencedTable"),
					rs.getString("ReferencedColumn"));
		}
	};
	private final JdbcOperations operations;
	private final HistoryDao historyDao;

	@Autowired
	public JdbcReferenceDaoImpl(DataSource ds, HistoryDao historyDao) {
		this.operations = Database.getOperations(ds);
		this.historyDao = historyDao;
	}

	@Override
	public Reference getReference(String table, String column) {
		QueryDefinition query = this.getReferenceQuery(table, column);
		QueryBuilder qb = Database.getQueryBuilder();
		List<Reference> result = this.operations.query(qb.toSql(query), this.referenceMapper);
		return result.isEmpty() ? null : (Reference) result.get(0);
	}

	public Reference getReferenceAt(String table, String column, int revision, SchemaDao schemaDao) {
		QueryDefinition query = this.getReferenceQuery(table, column);
		this.historyDao.historifyQuery(query, revision, schemaDao);
		QueryBuilder qb = Database.getQueryBuilder();
		List<Reference> result = this.operations.query(qb.toSql(query), this.referenceMapper);
		return result.isEmpty() ? null : (Reference) result.get(0);
	}

	private QueryDefinition getReferenceQuery(String table, String column) {
		QueryDefinition query = new QueryDefinition("Reference");
		query.select("Reference", "TableName");
		query.select("ReferenceColumn", "ColumnName");
		query.select("Reference", "ReferencedTable");
		query.select("ReferenceColumn", "ReferencedColumn");
		Join join = new Join("ReferenceColumn", Join.Type.INNER);
		join.newConstraint("Reference", "ReferenceID", Constraint.Operator.Equals, "ReferenceColumn", "ReferenceID");
		query.joinWith(join);
		query.newConstraint("Reference", "TableName", Constraint.Operator.Equals, table);
		query.newConstraint("ReferenceColumn", "ColumnName", Constraint.Operator.Equals, column);
		return query;
	}

	@Override
	public DataField getSourceField(String path) {
		String[] elements = path.split("\\.");
		String table = elements[0];
		String column = elements[1];
		if (elements.length > 2) {
			int i = 2;

			for (int n = elements.length; i < n; ++i) {
				Reference reference = this.getReference(table, column);
				if (reference == null) {
					return null;
				}

				table = reference.getReferencedTable();
				column = elements[i];
				if (i == n - 1) {
					return new DataField(table, column);
				}
			}
		}

		return new DataField(table, column);
	}

	@Override
	public DataField getSourceFieldAt(String path, int revision, SchemaDao schemaDao) {
		String[] elements = path.split("\\.");
		String table = elements[0];
		String column = elements[1];
		if (elements.length > 2) {
			int i = 2;

			for (int n = elements.length; i < n; ++i) {
				Reference reference = this.getReferenceAt(table, column, revision, schemaDao);
				if (reference == null) {
					return null;
				}

				table = reference.getReferencedTable();
				column = elements[i];
				if (i == n - 1) {
					return new DataField(table, column);
				}
			}
		}

		return new DataField(table, column);
	}
}
