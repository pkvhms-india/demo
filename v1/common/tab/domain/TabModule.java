package com.varicent.common.tab.domain;

import org.springframework.util.Assert;

public class TabModule extends Tab {
	private final ModuleType moduleType;

	public TabModule(Tab tabModule, ModuleType moduleType) {
		super(tabModule.getId(), tabModule.getParentId(), tabModule.getPreviousId(), tabModule.getName(),
				Tab.Type.WEB_MODULE, tabModule.getWorkflowObjectId());
		Assert.isTrue(tabModule.isModule());
		this.moduleType = moduleType;
	}

	@Override
	public String getUrl() {
		return this.moduleType.getUrl() + super.getUrl();
	}

	public static enum ModuleType {
		PAYEE_LEDGER(0, "/ledger/detail.html"), INQUIRIES(1, "/inquiries/index.html"),
		DATA_EDIT(2, "/dataedit/index.html"), WEB_FORMS(3, "/webforms/index.html"),
		REPORTING(4, "/reporting/index.html"), DRAWASSIGNMENT(5, "/drawassignment/index.html"),
		DRAWREPORT(6, "/drawreport/index.html"), COMPPLANMANAGER(7, "/compplanmanager/index.html");

		private final int value;
		private final String url;

		private ModuleType(int value, String url) {
			this.value = value;
			this.url = url;
		}

		public static ModuleType getType(int value) {
			for (ModuleType type : values()) {
				if (type.value == value) {
					return type;
				}
			}

			throw new IllegalArgumentException("Invalid module type value: " + value);
		}

		public String getUrl() {
			return this.url;
		}
	}
}
