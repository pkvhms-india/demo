package com.varicent.common.tab.domain;

import org.springframework.util.Assert;

public class ExternalUrlTab extends Tab {
	public ExternalUrlTab(Tab tab) {
		super(tab.getId(), tab.getParentId(), tab.getPreviousId(), tab.getName(), Tab.Type.EXTERNAL_URL,
				tab.getWorkflowObjectId());
		Assert.isTrue(tab.isExternalURL());
	}
}
