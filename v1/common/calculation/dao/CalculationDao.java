package com.varicent.common.calculation.dao;

import com.varicent.common.calculation.domain.Calculation;

public interface CalculationDao {
	String RESULT_TABLE_PREFIX = "_Result";

	Calculation getCalculation(int var1);

	int getResettingPartitionCount(int var1);
}
