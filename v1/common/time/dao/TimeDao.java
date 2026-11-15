package com.varicent.common.time.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.varicent.common.schema.dao.SchemaDao;
import com.varicent.common.time.domain.Frequency;
import com.varicent.common.time.domain.ResetMonitor;
import com.varicent.common.time.domain.RevisionLockPeriod;
import com.varicent.common.time.domain.TimePeriod;
import com.varicent.core.database.schema.ColumnDefinition;

public interface TimeDao {
	TimePeriod getFirstUnlockedPeriod(String var1);

	Frequency getFrequency(String var1);

	Frequency getFrequency(String var1, String var2);

	Date getSystemDate();

	ColumnDefinition getLockableDateColumn(String var1, SchemaDao var2);

	Date getLockedDate(String var1);

	Date getLockedDate(String var1, String var2);

	List<TimePeriod> getLowestTimePeriods(String var1);

	ResetMonitor getResetMonitor(String var1, String var2, String var3);

	TimePeriod getTimePeriod(String var1, String var2, String var3);

	TimePeriod getTimePeriod(String var1, String var2);

	Map<String, List<TimePeriod>> getAllTimePeriods();

	List<TimePeriod> getTimePeriods(String var1, boolean var2);

	List<TimePeriod> getTimePeriods(String var1, Date var2, Date var3);

	List<TimePeriod> getTimePeriods(String var1, String var2, boolean var3);

	boolean isLockedRow(String var1, List<Object> var2, SchemaDao var3);

	List<RevisionLockPeriod> getRevisionLockPeriods(String var1, String var2);

	boolean isDateValidForTable(Date var1, String var2);

	boolean isTableCustom(String var1);
}
