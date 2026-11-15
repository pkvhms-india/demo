package com.varicent.common.tab.dao;

import java.util.Map;

import com.varicent.common.tab.domain.TabModule;

public interface TabModuleDao {
	TabModule.ModuleType getModuleType(int var1);

	Map<Integer, Integer> getModuleMap();
}
