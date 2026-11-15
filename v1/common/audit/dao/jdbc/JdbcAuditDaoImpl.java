package com.varicent.common.audit.dao.jdbc;

import java.util.Locale;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import com.varicent.common.audit.dao.AuditDao;
import com.varicent.common.history.dao.jdbc.ActiveRevision;
import com.varicent.common.query.domain.FullInsertDefinition;
import com.varicent.common.query.domain.InsertDefinition;
import com.varicent.common.query.domain.Literal;
import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.query.sql.insert.FullInsertBuilder;
import com.varicent.common.query.sql.insert.InsertBuilder;
import com.varicent.core.database.Database;
import com.varicent.core.i18n.Message;
import com.varicent.core.i18n.Messages;
import com.varicent.security.domain.UserAccount;

@Repository("auditDao")
public class JdbcAuditDaoImpl implements AuditDao {
	private static final String[] INSERT_COLUMNS = new String[] { "UserType_", "UserID_", "Module_", "Event_", "Time_",
			"Message_" };
	private static final String[] REVISION_INSERT_COLUMNS = new String[] { "UserType_", "UserID_", "Module_", "Event_",
			"Time_", "Message_", "_Start", "_End" };
	private final JdbcOperations operations;
	private final Messages messages;

	@Autowired
	public JdbcAuditDaoImpl(DataSource ds, Messages messages) {
		this.operations = Database.getOperations(ds);
		this.messages = messages;
	}

	@Override
	public void log(Message module, Message event, Message message) {
		this.log(module, event, message, UserAccount.getCurrent());
	}

	@Override
	public void log(Message module, Message event, Message message, UserAccount user) {
		this.log(module, event, message, user.getPayeeId());
	}

	@Override
	public void log(Message module, Message event, Message message, String payeeId) {
		InsertDefinition insert = new InsertDefinition("Audit_", INSERT_COLUMNS);
		Locale locale = LocaleContextHolder.getLocale();
		insert.addValue(1);
		insert.addValue(payeeId);
		insert.addValue(this.messages.getHTMLDecodedMessage(module.getKey(), locale));
		insert.addValue(this.messages.getHTMLDecodedMessage(event.getKey(), locale));
		insert.addSystemUtcDateTime();
		insert.addValue(this
				.truncateMessage(this.messages.getHTMLDecodedMessage(message.getKey(), locale, message.getParams())));
		InsertBuilder ib = Database.getInsertBuilder();
		this.operations.update(ib.toSql(insert));
	}

	@Override
	public void logRevision(Message module, Message event, Message message) {
		QueryDefinition query = new QueryDefinition(ActiveRevision.TABLE_NAME);
		query.select(Literal.newLiteral(1), "_UserType");
		query.select(Literal.newLiteral(UserAccount.getCurrent().getPayeeId()), "_UserID");
		query.select(Literal.newLiteral(this.messages.getMessage(module)), "_Module");
		query.select(Literal.newLiteral(this.messages.getMessage(event)), "_Event");
		query.selectSystemUtcTime("_Time");
		query.select(Literal.newLiteral(this.truncateMessage(this.messages.getMessage(message))), "_Message");
		query.select(ActiveRevision.TABLE_NAME, "RevisionID", "_Start");
		query.select(ActiveRevision.TABLE_NAME, "RevisionID", "_End");
		FullInsertDefinition insert = new FullInsertDefinition("Audit_", REVISION_INSERT_COLUMNS, query);
		FullInsertBuilder fib = Database.getFullInsertBuilder();
		this.operations.update(fib.toSql(insert));
	}

	private String truncateMessage(String message) {
		return message != null && message.length() > 1000 ? message.substring(0, 1000) : message;
	}
}
