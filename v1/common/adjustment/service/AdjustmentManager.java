package com.varicent.common.adjustment.service;

import java.math.BigDecimal;

import com.varicent.common.payee.domain.Payee;
import com.varicent.common.plan.domain.Plan;

public interface AdjustmentManager {
	void addManualAdjustment(String var1, String var2, String var3, BigDecimal var4, String var5);

	Payee getPayee(String var1);

	Plan getPlan(String var1);
}
