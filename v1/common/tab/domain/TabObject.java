package com.varicent.common.tab.domain;

import org.springframework.util.Assert;

public class TabObject extends Tab {
	private final String objectId;

	public TabObject(Tab tabObject, String objectId) {
		super(tabObject.getId(), tabObject.getParentId(), tabObject.getPreviousId(), tabObject.getName(),
				tabObject.getType(), tabObject.getWorkflowObjectId());
		Assert.isTrue(tabObject.isObject());
		this.objectId = objectId;
	}

	public String getObjectId() {
		return this.objectId;
	}
}
