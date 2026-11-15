package com.varicent.common.tab.domain;

import java.util.Map;

public class LinkTabVerifier {
	private final Map<Integer, Integer> visibleTabIdWorkflowObjectIdMap;
	private final Map<Integer, Integer> webReportIdWorkflowObjectIdMap;
	private final Map<Integer, Integer> webFormIdWorkflowObjectIdMap;

	public LinkTabVerifier(Map<Integer, Integer> visibleTabIdWorkflowObjectIdMap,
			Map<Integer, Integer> webReportIdWorkflowObjectIdMap, Map<Integer, Integer> webFormIdWorkflowObjectIdMap) {
		this.visibleTabIdWorkflowObjectIdMap = visibleTabIdWorkflowObjectIdMap;
		this.webReportIdWorkflowObjectIdMap = webReportIdWorkflowObjectIdMap;
		this.webFormIdWorkflowObjectIdMap = webFormIdWorkflowObjectIdMap;
	}

	public boolean isWebFormInTheTab(Integer webFormId, Integer webTabId) {
		if (!this.visibleTabIdWorkflowObjectIdMap.containsKey(webTabId)) {
			return false;
		} else if (!this.webFormIdWorkflowObjectIdMap.containsKey(webFormId)) {
			return false;
		} else {
			return this.visibleTabIdWorkflowObjectIdMap.get(webTabId) == this.webFormIdWorkflowObjectIdMap
					.get(webFormId);
		}
	}

	public boolean isWebReportInTheTab(Integer webReportId, Integer webTabId) {
		if (!this.visibleTabIdWorkflowObjectIdMap.containsKey(webTabId)) {
			return false;
		} else if (!this.webReportIdWorkflowObjectIdMap.containsKey(webReportId)) {
			return false;
		} else {
			return this.visibleTabIdWorkflowObjectIdMap.get(webTabId) == this.webReportIdWorkflowObjectIdMap
					.get(webReportId);
		}
	}
}
