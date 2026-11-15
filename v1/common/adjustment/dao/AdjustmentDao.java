package com.varicent.common.adjustment.dao;

import java.util.List;

import com.varicent.common.adjustment.domain.Adjustment;
import com.varicent.common.time.domain.TimePeriod;

public interface AdjustmentDao {
	void addManualAdjustment(Adjustment var1);

	List<Adjustment> getManualAdjustments(String var1, String var2, TimePeriod var3);
}
