package com.varicent.common.payee.dao.jdbc;

import com.varicent.common.query.domain.Constraint;
import com.varicent.common.query.domain.QueryDefinition;

public class PayeeQuery {
	public static QueryDefinition getPayeeGroupQuery(String payeeId) {
		QueryDefinition query = new QueryDefinition("GroupMember_");
		query.newConstraint("GroupMember_", "PayeeID_", Constraint.Operator.Equals, payeeId);
		return query;
	}
}
