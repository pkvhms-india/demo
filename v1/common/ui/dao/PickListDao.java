package com.varicent.common.ui.dao;

import java.util.List;

import com.varicent.common.query.domain.SourceDefinition;
import com.varicent.common.ui.domain.SelectOption;
import com.varicent.reporting.dao.jdbc.BaseBufferMerger;
import com.varicent.reporting.domain.PicklistDisplayOptions;
import com.varicent.webform.dao.jdbc.BufferMerger;

public interface PickListDao {
	List<SelectOption> getOptions(String var1, String var2);

	List<SelectOption> getWebFormOptions(String var1, String var2, BufferMerger var3);

	List<SelectOption> getOptions(String var1, String var2, SourceDefinition var3);

	List<SelectOption> getWebReportOptions(String var1, String var2, SourceDefinition var3, BaseBufferMerger var4,
			PicklistDisplayOptions var5);

	List<SelectOption> getWorkflowOptions(String var1);
}
