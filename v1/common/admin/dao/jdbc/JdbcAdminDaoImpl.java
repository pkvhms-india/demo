package com.varicent.common.admin.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.varicent.common.admin.dao.AdminDao;
import com.varicent.common.admin.domain.Admin;
import com.varicent.common.admin.domain.AdminNote;
import com.varicent.common.query.domain.Constraint;
import com.varicent.common.query.domain.InsertDefinition;
import com.varicent.common.query.domain.Join;
import com.varicent.common.query.domain.Literal;
import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.query.sql.QueryBuilder;
import com.varicent.common.query.sql.insert.InsertBuilder;
import com.varicent.core.database.Database;
import com.varicent.messages.domain.Message;
import com.varicent.security.domain.UserAccount;

@Repository("adminDao")
public class JdbcAdminDaoImpl implements AdminDao {
	final String AdminPayeeNotes = "AdminPayeeNotes";
	final String AdminPayeeNoteVariables = "AdminPayeeNoteVariables";
	private final RowMapper<AdminNote> noteMapper = new RowMapper<AdminNote>() {
		@Override
		public AdminNote mapRow(ResultSet rs, int rowNumber) throws SQLException {
			Admin admin = new Admin(rs.getString("UserID"),
					rs.getString("Name") == null ? rs.getString("UserID") : rs.getString("Name"),
					rs.getString("Email") == null ? "" : rs.getString("Email"));
			return new AdminNote(rs.getInt("NoteID"), admin, rs.getTimestamp("Time"), rs.getString("Subject"),
					rs.getString("Body"), Message.Category.toCategory(rs.getInt("Category")));
		}
	};
	private final JdbcOperations operations;

	@Autowired
	public JdbcAdminDaoImpl(DataSource ds) {
		this.operations = Database.getOperations(ds);
	}

	@Override
	public AdminNote getAdminPayeeNote(int id) {
		QueryDefinition query = new QueryDefinition("AdminPayeeNotes");
		query.select("AdminPayeeNotes", "NoteID");
		query.select("AdminPayeeNotes", "UserID");
		query.select("AdminPayeeNotes", "Time");
		query.select("AdminPayeeNotes", "Subject");
		query.select("AdminPayeeNotes", "Body");
		query.select("Admin", "AdminID");
		query.select("Admin", "Name");
		query.select("Admin", "Email");
		query.select(Literal.newLiteral(5), "Category");
		query.newConstraint("AdminPayeeNotes", "NoteID", Constraint.Operator.Equals, id);
		query.newConstraint("AdminPayeeNotes", "PayeeID", Constraint.Operator.Equals,
				UserAccount.getCurrent().getPayeeId());
		Join adminJoin = new Join("Admin", Join.Type.LEFT);
		adminJoin.newConstraint("AdminPayeeNotes", "UserID", Constraint.Operator.Equals, "Admin", "AdminID");
		query.joinWith(adminJoin);
		QueryBuilder qb = Database.getQueryBuilder();
		AdminNote note = (AdminNote) this.operations.queryForObject(qb.toSql(query), this.noteMapper);
		note = this.ReplaceNoteVariables(note);
		return note;
	}

	private AdminNote ReplaceNoteVariables(AdminNote note) {
		QueryDefinition query = new QueryDefinition("AdminPayeeNoteVariables");
		query.select("AdminPayeeNoteVariables", "Variable");
		query.select("AdminPayeeNoteVariables", "Value");
		query.newConstraint("AdminPayeeNoteVariables", "NoteID", Constraint.Operator.Equals, note.getId());
		QueryBuilder qb = Database.getQueryBuilder();
		List<Map<String, Object>> variables = this.operations.queryForList(qb.toSql(query));
		String[] body = note.getBody();
		String subject = note.getSubject();

		for (Map<String, Object> var : variables) {
			String variable = (String) var.get("Variable");
			String value = (String) var.get("Value");
			subject = subject.replace(variable, value);

			for (int i = 0; i < body.length; ++i) {
				body[i] = body[i].replace(variable, value);
			}
		}

		String rebuiltBody = "";

		for (String bodyLine : body) {
			rebuiltBody = rebuiltBody + bodyLine;
			rebuiltBody = rebuiltBody + "\r\n";
		}

		return new AdminNote(note.getId(), note.getAdmin(), note.getPosted(), subject, rebuiltBody,
				Message.Category.ADMIN_PAYEE_MESSAGE);
	}

	@Override
	public AdminNote getAdminNote(int id) {
		QueryDefinition query = this.getAdminNoteQuery();
		query.newConstraint("AdminNotes", "NoteID", Constraint.Operator.Equals, id);
		QueryBuilder qb = Database.getQueryBuilder();
		return (AdminNote) this.operations.queryForObject(qb.toSql(query), this.noteMapper);
	}

	@Override
	public List<AdminNote> getAdminNotes() {
		return this.loadAdminNotes(0);
	}

	@Override
	public List<AdminNote> getAdminNotes(int limit) {
		return this.loadAdminNotes(limit);
	}

	private List<AdminNote> loadAdminNotes(int limit) {
		UserAccount user = UserAccount.getCurrent();
		QueryDefinition query = this.getAdminNoteQuery();
		query.union(this.getAdminPayeeNoteQuery(user.getPayeeId()));
		if (limit > 0) {
			query.limitTo(limit);
		}

		QueryDefinition outterQuery = new QueryDefinition(query, "AdminNotes");
		outterQuery.select("AdminNotes", "NoteID");
		outterQuery.select("AdminNotes", "UserID");
		outterQuery.select("AdminNotes", "Time");
		outterQuery.select("AdminNotes", "Subject");
		outterQuery.select("AdminNotes", "Body");
		outterQuery.select("AdminNotes", "AdminID");
		outterQuery.select("AdminNotes", "Name");
		outterQuery.select("AdminNotes", "Email");
		outterQuery.select("AdminNotes", "Category");
		outterQuery.orderBy("AdminNotes", "Time", true);
		outterQuery.orderBy("AdminNotes", "NoteID", true);
		QueryBuilder qb = Database.getQueryBuilder();
		return this.operations.query(qb.toSql(outterQuery), this.noteMapper);
	}

	private QueryDefinition getAdminNoteQuery() {
		QueryDefinition query = new QueryDefinition("AdminNotes");
		query.select("AdminNotes", "NoteID");
		query.select("AdminNotes", "UserID");
		query.select("AdminNotes", "Time");
		query.select("AdminNotes", "Subject");
		query.select("AdminNotes", "Body");
		query.select("Admin", "AdminID");
		query.select("Admin", "Name");
		query.select("Admin", "Email");
		query.select(Literal.newLiteral(0), "Category");
		Join adminJoin = new Join("Admin", Join.Type.LEFT);
		adminJoin.newConstraint("AdminNotes", "UserID", Constraint.Operator.Equals, "Admin", "AdminID");
		query.joinWith(adminJoin);
		return query;
	}

	private QueryDefinition getAdminPayeeNoteQuery(String payeeID) {
		QueryDefinition query = new QueryDefinition("AdminPayeeNotes");
		query.select("AdminPayeeNotes", "NoteID");
		query.select("AdminPayeeNotes", "UserID");
		query.select("AdminPayeeNotes", "Time");
		query.select("AdminPayeeNotes", "Subject");
		query.select("AdminPayeeNotes", "Body");
		query.select("Admin", "AdminID");
		query.select("Admin", "Name");
		query.select("Admin", "Email");
		query.select(Literal.newLiteral(5), "Category");
		Join adminJoin = new Join("Admin", Join.Type.LEFT);
		adminJoin.newConstraint("AdminPayeeNotes", "UserID", Constraint.Operator.Equals, "Admin", "AdminID");
		query.joinWith(adminJoin);
		query.newConstraint("AdminPayeeNotes", "PayeeID", Constraint.Operator.Equals, payeeID);
		return query;
	}

	@Override
	public void Insert(String subject, String body, String payeeID) {
		this.Insert("", subject, body, payeeID);
	}

	@Override
	public void InsertVariable(int noteID, String variable, String value) {
		InsertDefinition insert = new InsertDefinition("AdminPayeeNoteVariables",
				new String[] { "NoteID", "Variable", "Value" });
		insert.addValue(noteID);
		insert.addValue(variable);
		insert.addValue(value);
		InsertBuilder ib = Database.getInsertBuilder();
		this.operations.update(ib.toSql(insert));
	}

	@Override
	public void Insert(String userID, String subject, String body, String payeeID) {
		InsertDefinition insert = new InsertDefinition("AdminPayeeNotes",
				new String[] { "UserID", "Time", "Subject", "Body", "PayeeID" });
		insert.addValue(userID);
		insert.addSystemDateTime();
		insert.addValue(subject);
		insert.addValue(body);
		insert.addValue(payeeID);
		InsertBuilder ib = Database.getInsertBuilder();
		this.operations.update(ib.toSql(insert));
	}

	@Override
	public int getMaxAdminNoteId() {
		QueryDefinition qd = new QueryDefinition("AdminPayeeNotes");
		qd.selectMax("AdminPayeeNotes", "NoteID", "MaxVal");
		QueryBuilder qb = Database.getQueryBuilder();
		return (Integer) this.operations.queryForObject(qb.toSql(qd), Integer.class);
	}
}
