package com.varicent.common.history.dao.jdbc;

import com.varicent.common.query.domain.Literal;
import com.varicent.core.database.Database;

public class ActiveRevision {
	public static final String TABLE_NAME = Database.getSyntax().getTempTablePrefix() + "HistoryActiveRevision";
	public static final String REVISION_COLUMN_NAME = "RevisionID";
	public static final String CREATE_TABLE_SQL;
	public static final String DROP_TABLE_SQL;

	private ActiveRevision() {
	}

	static {
		CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + "(" + "RevisionID" + " "
				+ Database.getSyntax().toDatabaseType(Literal.Type.INTEGER) + ")";
		DROP_TABLE_SQL = "DROP TABLE " + TABLE_NAME;
	}
}
