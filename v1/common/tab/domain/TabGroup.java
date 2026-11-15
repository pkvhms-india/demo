package com.varicent.common.tab.domain;

import java.util.List;

import org.springframework.util.Assert;

public class TabGroup extends Tab {
	private final List<Tab> subTabs;

	public TabGroup(Tab tabGroup, List<Tab> subTabs) {
		super(tabGroup.getId(), tabGroup.getParentId(), tabGroup.getPreviousId(), tabGroup.getName(),
				Tab.Type.TAB_GROUP, tabGroup.getWorkflowObjectId());
		Assert.isTrue(tabGroup.isTabGroup());

		for (Tab subTab : subTabs) {
			subTab.setTabGroupId(tabGroup.getId());
		}

		this.subTabs = subTabs;
	}

	public List<Tab> getSubTabs() {
		return this.subTabs;
	}

	@Override
	public String getUrl() {
		if (this.hasSubTabs()) {
			Tab firstSubTab = this.subTabs.get(0);
			return firstSubTab.getUrl();
		} else {
			return super.getUrl();
		}
	}

	public boolean hasSubTabs() {
		return !this.subTabs.isEmpty();
	}
}
