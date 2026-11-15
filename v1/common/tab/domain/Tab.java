package com.varicent.common.tab.domain;

import org.springframework.util.Assert;

import com.varicent.workflow.domain.WorkflowObject;

public class Tab {
	public static final String TAB_ID_PARAMETER = "tabId";
	private final int id;
	private final Integer parentId;
	private final Integer previousId;
	private final String name;
	private final Type type;
	private final int workflowObjectId;
	private Integer tabGroupId;

	public Tab(int id, Integer parentId, Integer previousId, String name, Type type, int workflowObjectId) {
		this.id = id;
		this.parentId = parentId;
		this.previousId = previousId;
		this.name = name;
		this.type = type;
		this.workflowObjectId = workflowObjectId;
	}

	public int getId() {
		return this.id;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public Integer getPreviousId() {
		return this.previousId;
	}

	public String getName() {
		return this.name;
	}

	public String getParameterUrl() {
		String url = this.getTabIdParameterUrl();
		return url;
	}

	public int getTabGroupId() {
		Assert.state(this.isSubTab());
		return this.tabGroupId;
	}

	private String getTabIdParameterUrl() {
		return "?tabId=" + this.getId();
	}

	public Type getType() {
		return this.type;
	}

	public String getUrl() {
		String url = "";
		if (this.type.hasUrl()) {
			url = this.type.getUrl();
		}

		return url + this.getParameterUrl();
	}

	public int getWorkflowObjectId() {
		return this.workflowObjectId;
	}

	public static WorkflowObject.Type getWorkflowObjectType(Type type) {
		switch (type) {
		case COMPENSATION_PLAN:
			return WorkflowObject.Type.COMP_PLAN;
		case TAILORED_REPORT:
			return WorkflowObject.Type.TAILORED_REPORT;
		case WEB_FORM:
			return WorkflowObject.Type.WEB_FORM;
		case PRESENTER_REPORT:
			return WorkflowObject.Type.WEB_REPORT;
		case DRAW_ASSIGNMENT:
			return WorkflowObject.Type.DRAW_ASSIGNMENT;
		case DRAW_REPORT:
			return WorkflowObject.Type.DRAW_REPORT;
		default:
			Assert.state(false);
			return WorkflowObject.Type.TAB;
		}
	}

	public boolean isCompensationPlan() {
		return this.type == Tab.Type.COMPENSATION_PLAN;
	}

	public boolean isDrawAssignment() {
		return this.type == Tab.Type.DRAW_ASSIGNMENT;
	}

	public boolean isModule() {
		return this.type == Tab.Type.WEB_MODULE;
	}

	public boolean isObject() {
		return !this.isModule() && !this.isTabGroup() && !this.isExternalURL();
	}

	public boolean isPresenterReport() {
		return this.type == Tab.Type.PRESENTER_REPORT;
	}

	public boolean isSubTab() {
		return this.tabGroupId != null;
	}

	public boolean isTabGroup() {
		return this.type == Tab.Type.TAB_GROUP;
	}

	public boolean isTailoredReport() {
		return this.type == Tab.Type.TAILORED_REPORT;
	}

	public boolean isWebForm() {
		return this.type == Tab.Type.WEB_FORM;
	}

	public boolean isExternalURL() {
		return this.type == Tab.Type.EXTERNAL_URL;
	}

	public void setTabGroupId(int tabGroupId) {
		this.tabGroupId = new Integer(tabGroupId);
	}

	public static enum Type {
		WEB_MODULE(0, (String) null), TAB_GROUP(1, (String) null), COMPENSATION_PLAN(2, "/ledger/detail.html"),
		TAILORED_REPORT(3, "/ledger/detail.html"), WEB_FORM(4, "/webforms/index.html"),
		PRESENTER_REPORT(5, "/reporting/index.html"), DRAW_ASSIGNMENT(8, "/drawassignment/index.html"),
		DRAW_REPORT(9, "/drawreport/index.html"), EXTERNAL_URL(10, "/externalurl/index.html"),
		COMPPLANMANAGER(11, "/compplanmanager/index.html");

		private final int value;
		private final String url;

		private Type(int value, String url) {
			this.value = value;
			this.url = url;
		}

		public static Type getType(int value) {
			for (Type type : values()) {
				if (type.value == value) {
					return type;
				}
			}

			throw new IllegalArgumentException("Invalid tab type value: " + value);
		}

		public String getUrl() {
			return this.url;
		}

		public boolean hasUrl() {
			return this.url != null;
		}
	}
}
