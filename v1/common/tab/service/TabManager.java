package com.varicent.common.tab.service;

import java.util.List;

import com.varicent.common.tab.domain.MessageTab;
import com.varicent.common.tab.domain.Tab;

public interface TabManager {
	List<Tab> getTabs();

	List<MessageTab> getMessageTabs();

	String getExternalUrl(int var1);
}
