package com.varicent.common.history.dao;

import java.util.Date;

import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.schema.dao.SchemaDao;
import com.varicent.core.database.schema.TableDefinition;

public interface HistoryDao {
	String HISTORY_TABLE_PREFIX = "_History";
	String HISTORY_TABLE_COLUMN_START = "_Start";
	String HISTORY_TABLE_COLUMN_END = "_End";

	void assignRevision();

	void beginRevision();

	void endRevision();

	QueryDefinition getHistoryDataQuery(TableDefinition var1, int var2);

	TableDefinition getHistoryTable(String var1, int var2, SchemaDao var3);

	int getNewRevision();

	int getLatestRevision();

	Date getRevisionTimestamp(int var1);

	QueryDefinition historifyQuery(QueryDefinition var1, int var2, SchemaDao var3);

	boolean isSystemView(String var1);

	boolean isStaticHistoryTable(String var1);
}
