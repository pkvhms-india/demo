package com.varicent.common.dataStore;

import java.util.Map;

import com.varicent.common.query.domain.SourceDefinition;

public interface DataStoreDao {
	Map<Integer, SourceDefinition> getDataStoreMap();
}
