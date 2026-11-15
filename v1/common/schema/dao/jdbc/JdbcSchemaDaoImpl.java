package com.varicent.common.schema.dao.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import com.varicent.common.schema.dao.SchemaDaoBase;
import com.varicent.core.database.Database;
import com.varicent.core.database.portability.SqlSyntax;
import com.varicent.core.database.schema.ColumnDefinition;
import com.varicent.core.database.schema.Schema;

@Repository("schemaDao")
public class JdbcSchemaDaoImpl extends SchemaDaoBase {
	private final DataSource ds;

	@Autowired
	public JdbcSchemaDaoImpl(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public List<ColumnDefinition> getColumns(String table) {
		SqlSyntax syntax = Database.getSyntax();
		return this.getColumns(syntax.toValidIdentifier(table), false);
	}

	@Override
	public List<ColumnDefinition> getKeyColumns(String table) {
		SqlSyntax syntax = Database.getSyntax();
		return this.getColumns(syntax.toValidIdentifier(table), true);
	}

	private List<ColumnDefinition> getColumns(String table, boolean keysOnly) {
		List<ColumnDefinition> columns = new ArrayList();
		Connection connection = null;

		try {
			connection = DataSourceUtils.getConnection(this.ds);
			PreparedStatement ps = connection
					.prepareStatement("SELECT * FROM " + Database.getSyntax().quote(table) + " WHERE 1 = 0");
			ResultSetMetaData metadata = ps.getMetaData();
			ResultSet keyResult = connection.getMetaData().getPrimaryKeys((String) null, (String) null, table);
			Set<String> keys = new HashSet();

			while (keyResult.next()) {
				keys.add(keyResult.getString("COLUMN_NAME"));
			}

			keyResult.close();
			int i = 1;

			for (int n = metadata.getColumnCount(); i <= n; ++i) {
				String name = metadata.getColumnName(i);
				if (!name.equals("EffStart_") && !name.equals("EffEnd_")) {
					boolean isKey = keys.contains(name);
					if (!keysOnly || isKey) {
						int type = metadata.getColumnType(i);
						if (Schema.isSqlServerDateColumn(metadata, i)) {
							type = 91;
						}

						boolean isNullable = metadata.isNullable(i) == 1;
						columns.add(new ColumnDefinition(table, name, type, i - 1, isKey, isNullable));
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			DataSourceUtils.releaseConnection(connection, this.ds);
		}

		return columns;
	}

	private List<ColumnDefinition> getColumnsSafe(String table, boolean keysOnly) {
		List<ColumnDefinition> columns = new ArrayList();
		Connection connection = null;

		try {
			connection = DataSourceUtils.getConnection(this.ds);
			DatabaseMetaData metadata = connection.getMetaData();
			ResultSet keyResult = metadata.getPrimaryKeys((String) null, (String) null, table);
			Set<String> keys = new HashSet();

			while (keyResult.next()) {
				keys.add(keyResult.getString("COLUMN_NAME"));
			}

			keyResult.close();
			ResultSet rs = metadata.getColumns((String) null, (String) null, table, (String) null);

			while (rs.next()) {
				String name = rs.getString("COLUMN_NAME");
				boolean isKey = keys.contains(name);
				if (!keysOnly || isKey) {
					int type = rs.getInt("DATA_TYPE");
					int index = rs.getInt("ORDINAL_POSITION") - 1;
					boolean isNullable = 1 == rs.getShort("NULLABLE");
					columns.add(new ColumnDefinition(table, name, type, index, isKey, isNullable));
				}
			}

			rs.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			DataSourceUtils.releaseConnection(connection, this.ds);
		}

		return columns;
	}
}
