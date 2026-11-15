package com.varicent.common.tab.dao;

import java.util.List;
import java.util.Map;

import com.varicent.common.payee.domain.Payee;
import com.varicent.common.tab.domain.Tab;
import com.varicent.workflow.domain.WorkflowObject;

public interface TabDao {
	String getObjectId(WorkflowObject.Type var1, int var2);

	Map<Integer, WorkflowObject> loadObjectIDs();

	List<Tab> getTabs();

	List<Integer> getViewableTabIds();

	List<Integer> getViewableTabIds(Payee var1);
}
