package com.varicent.common.attachment.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import com.varicent.common.query.domain.Clause;
import com.varicent.common.query.domain.Constraint;
import com.varicent.common.query.domain.Join;
import com.varicent.common.query.domain.Literal;
import com.varicent.common.query.domain.OrClause;
import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.query.domain.SelectItem;
import com.varicent.security.domain.UserAccount;
import com.varicent.workflow.dao.jdbc.WorkflowQuery;
import com.varicent.workflow.domain.WorkflowObject;

public class AttachmentQuery {
	public static QueryDefinition getWorkflowAttachmentQuery() {
		return getWorkflowAttachmentQuery(new ArrayList());
	}

	public static QueryDefinition getWorkflowAttachmentQuery(List<Integer> documentIds) {
		QueryDefinition query = new QueryDefinition("Document");
		query.select("Document", "DocumentID");
		query.select("Document", "Name");
		query.select("Document", "Time");
		if (!documentIds.isEmpty()) {
			Clause clause = new OrClause();

			for (int id : documentIds) {
				clause.newConstraint("Document", "DocumentID", Constraint.Operator.Equals, id);
			}

			query.addConstraint(clause);
		}

		return query;
	}

	public static QueryDefinition getWorkflowAttachmentMessageQuery() {
		UserAccount user = UserAccount.getCurrent();
		QueryDefinition query = WorkflowQuery.getViewableObjectsQuery(user.getPayeeId(), WorkflowObject.Type.DOCUMENT);
		SelectItem select = (SelectItem) query.getSelect().remove(0);
		String table = select.getTable();
		String column = select.getColumn();
		Join documentJoin = new Join("Document", Join.Type.INNER);
		documentJoin.newConstraint(table, column, Constraint.Operator.Equals, "Document", "DocumentID");
		query.select(Literal.newNullLiteral(), "LastCommentDate");
		query.select("Document", "Time");
		query.select(Literal.newNullLiteral(), "StartTime");
		query.select(Literal.newLiteral(1), "Category");
		query.select("Document", "DocumentID", "ID", SelectItem.Type.STRING);
		query.select(Literal.newNullLiteral(), "From");
		query.select("Document", "Name", "Summary");
		return query;
	}
}
