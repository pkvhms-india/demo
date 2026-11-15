package com.varicent.common.payee.dao;

import java.util.Set;

import com.varicent.common.payee.domain.Payee;

public interface PayeeDao {
	Payee getPayee(String var1);

	Payee getPayeeByKey(String var1);

	Payee getPayeeByEmail(String var1);

	Set<String> getPayeeGroupIds(String var1);
}
