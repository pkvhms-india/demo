package com.varicent.common.port.dao;

import java.util.Map;

import com.varicent.common.history.dao.HistoryDao;
import com.varicent.common.port.domain.Port;
import com.varicent.common.schema.dao.SchemaDao;

public interface PortDao {
	Map<String, Port> getPortMap();

	Map<String, Port> getPortMapAt(int var1, HistoryDao var2, SchemaDao var3);
}
