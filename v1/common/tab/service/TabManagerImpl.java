package com.varicent.common.tab.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.varicent.common.tab.dao.ExternalUrlDao;
import com.varicent.common.tab.dao.TabDao;
import com.varicent.common.tab.dao.TabModuleDao;
import com.varicent.common.tab.domain.ExternalUrlTab;
import com.varicent.common.tab.domain.MessageTab;
import com.varicent.common.tab.domain.Tab;
import com.varicent.common.tab.domain.TabGroup;
import com.varicent.common.tab.domain.TabModule;
import com.varicent.common.tab.domain.TabObject;
import com.varicent.workflow.domain.WorkflowObject;

@Service("tabManager")
@Transactional(readOnly = true)
public class TabManagerImpl implements TabManager {
	private final ExternalUrlDao externalUrlDao;
	private final TabDao tabDao;
	private final TabModuleDao tabModuleDao;

	@Autowired
	public TabManagerImpl(TabDao tabDao, TabModuleDao tabModuleDao, ExternalUrlDao externalUrlDao) {
		this.tabDao = tabDao;
		this.tabModuleDao = tabModuleDao;
		this.externalUrlDao = externalUrlDao;
	}

	private void addTabGroup(Tab tabGroup, List<Tab> allTabs, List<Integer> viewableTabIds, List<Tab> tabs,
			Map<Integer, WorkflowObject> objectIDs, Map<Integer, Integer> moduleMap) {
		Assert.isTrue(tabGroup.isTabGroup());
		List<Tab> subTabs = getTabs(allTabs, tabGroup.getId());
		List<Tab> viewableSubTabs = new ArrayList();

		for (Tab subTab : subTabs) {
			if (viewableTabIds.contains(subTab.getId())) {
				if (subTab.isModule()) {
					this.addTabModule(subTab, viewableSubTabs, objectIDs, moduleMap);
				} else if (subTab.isObject()) {
					this.addTabObject(subTab, viewableSubTabs, objectIDs);
				} else if (subTab.isExternalURL()) {
					this.addExternalUrlTab(subTab, viewableSubTabs);
				}
			}
		}

		if (viewableSubTabs.size() != 0) {
			if (viewableSubTabs.size() == 1) {
				tabs.add(viewableSubTabs.get(0));
			} else {
				tabs.add(new TabGroup(tabGroup, viewableSubTabs));
			}

		}
	}

	private void addTabModule(Tab tabModule, List<Tab> tabs, Map<Integer, WorkflowObject> objectIDs,
			Map<Integer, Integer> moduleTypes) {
		Assert.isTrue(tabModule.isModule());
		TabModule.ModuleType type = TabModule.ModuleType.getType(moduleTypes.get(tabModule.getWorkflowObjectId()));
		tabs.add(new TabModule(tabModule, type));
	}

	private void addTabObject(Tab tabObject, List<Tab> tabs, Map<Integer, WorkflowObject> objectIDs) {
		Assert.isTrue(tabObject.isObject());
		String objectId = objectIDs.get(tabObject.getWorkflowObjectId()).getId();
		tabs.add(new TabObject(tabObject, objectId));
	}

	private void addExternalUrlTab(Tab tab, List<Tab> tabs) {
		Assert.isTrue(tab.isExternalURL());
		tabs.add(new ExternalUrlTab(tab));
	}

	private static boolean areEqualIntegers(Integer i, Integer j) {
		if (i == null && j == null) {
			return true;
		} else {
			return i != null && j != null ? i.equals(j) : false;
		}
	}

	@Override
	public List<Tab> getTabs() {
		List<Tab> allTabs = this.tabDao.getTabs();
		List<Integer> viewableTabIds = this.tabDao.getViewableTabIds();
		List<Tab> rootTabs = getTabs(allTabs, (Integer) null);
		List<Tab> viewableTabs = new ArrayList();
		Map<Integer, WorkflowObject> objectIDs = this.tabDao.loadObjectIDs();
		Map<Integer, Integer> moduleMap = this.tabModuleDao.getModuleMap();

		for (Tab rootTab : rootTabs) {
			if (viewableTabIds.contains(rootTab.getId())) {
				if (rootTab.isModule()) {
					this.addTabModule(rootTab, viewableTabs, objectIDs, moduleMap);
				} else if (rootTab.isObject()) {
					this.addTabObject(rootTab, viewableTabs, objectIDs);
				} else if (rootTab.isTabGroup()) {
					this.addTabGroup(rootTab, allTabs, viewableTabIds, viewableTabs, objectIDs, moduleMap);
				} else if (rootTab.isExternalURL()) {
					this.addExternalUrlTab(rootTab, viewableTabs);
				}
			}
		}

		return viewableTabs;
	}

	private static List<Tab> getTabs(List<Tab> allTabs, Integer parentId) {
		List<Tab> tabs = new ArrayList();

		for (Tab tab : allTabs) {
			if (areEqualIntegers(parentId, tab.getParentId())) {
				tabs.add(tab);
			}
		}

		return orderTabs(tabs);
	}

	private static List<Tab> orderTabs(List<Tab> tabs) {
		List<Tab> orderedTabs = new ArrayList();
		Integer previousId = null;

		for (int count = 0; count < tabs.size(); ++count) {
			for (Tab tab : tabs) {
				if (areEqualIntegers(previousId, tab.getPreviousId())) {
					orderedTabs.add(tab);
					previousId = new Integer(tab.getId());
					break;
				}
			}
		}

		return orderedTabs;
	}

	@Override
	public List<MessageTab> getMessageTabs() {
		List<MessageTab> tabs = new ArrayList();
		tabs.add(MessageTab.INBOX);
		tabs.add(MessageTab.ADMIN);
		tabs.add(MessageTab.INQUIRIES);
		tabs.add(MessageTab.DOCUMENTS);
		tabs.add(MessageTab.TASKS);
		tabs.add(MessageTab.BPM);
		return tabs;
	}

	@Override
	public String getExternalUrl(int tabId) {
		return this.externalUrlDao.getUrl(tabId);
	}
}
