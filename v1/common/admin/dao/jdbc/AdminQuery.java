package com.varicent.common.admin.dao.jdbc;

import com.varicent.common.query.domain.Constraint;
import com.varicent.common.query.domain.Literal;
import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.query.domain.SelectItem;
import com.varicent.security.domain.UserAccount;

public class AdminQuery {
	static String AdminPayeeNotes = "AdminPayeeNotes";
	static String NoteID = "NoteID";
	static String AdminNotes = "AdminNotes";
	static String PayeeID = "PayeeID";

	public static QueryDefinition getAdminMessageQuery() {
		QueryDefinition query = new QueryDefinition("AdminNotes");
		query.select(Literal.newNullLiteral(), "LastCommentDate");
		query.select("AdminNotes", "Time");
		query.select(Literal.newNullLiteral(), "StartTime");
		query.select(Literal.newLiteral(0), "Category");
		query.select("AdminNotes", "NoteID", "ID", SelectItem.Type.STRING);
		query.select("AdminNotes", "UserID", "From");
		query.select("AdminNotes", "Subject", "Summary");
		QueryDefinition individualMsg = new QueryDefinition(AdminPayeeNotes);
		individualMsg.select(Literal.newNullLiteral(), "LastCommentDate");
		individualMsg.select(AdminPayeeNotes, "Time");
		individualMsg.select(Literal.newNullLiteral(), "StartTime");
		individualMsg.select(Literal.newLiteral(5), "Category");
		individualMsg.select(AdminPayeeNotes, "NoteID", "ID", SelectItem.Type.STRING);
		individualMsg.select(AdminPayeeNotes, "UserID", "From");
		individualMsg.select(AdminPayeeNotes, "Subject", "Summary");
		individualMsg.newConstraint(AdminPayeeNotes, PayeeID, Constraint.Operator.Equals,
				UserAccount.getCurrent().getPayeeId());
		query.union(individualMsg);
		return query;
	}
}
