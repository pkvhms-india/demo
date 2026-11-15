package com.varicent.common.attachment.dao.jdbc;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;
import org.springframework.util.FileCopyUtils;

import com.varicent.common.attachment.dao.AttachmentDao;
import com.varicent.common.attachment.domain.Attachment;
import com.varicent.common.query.domain.Constraint;
import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.query.sql.QueryBuilder;
import com.varicent.core.database.Database;
import com.varicent.core.database.support.RowMapperUtil;
import com.varicent.security.domain.UserAccount;
import com.varicent.workflow.dao.jdbc.WorkflowQuery;
import com.varicent.workflow.domain.WorkflowObject;

@Repository("attachmentDao")
public class JdbcAttachmentDaoImpl implements AttachmentDao {
	private final RowMapper<Attachment> workflowDocumentMapper = new RowMapper<Attachment>() {
		@Override
		public Attachment mapRow(ResultSet rs, int rowNumber) throws SQLException {
			String fileName = rs.getString("Name");
			Attachment.FileType fileType = Attachment.FileType.OTHER;
			if (fileName.endsWith(".pdf")) {
				fileType = Attachment.FileType.PDF;
			} else if (!fileName.endsWith(".doc") && !fileName.endsWith(".docx")) {
				if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
					if (fileName.endsWith(".ppt") || fileName.endsWith(".pptx")) {
						fileType = Attachment.FileType.PPT;
					}
				} else {
					fileType = Attachment.FileType.XLS;
				}
			} else {
				fileType = Attachment.FileType.DOC;
			}

			return new Attachment("Document", "DocumentID", rs.getString("DocumentID"), fileName, fileType,
					rs.getTimestamp("Time"));
		}
	};
	private final JdbcOperations operations;
	private final LobHandler lobHandler;

	@Autowired
	public JdbcAttachmentDaoImpl(DataSource ds, LobHandler lobHandler) {
		this.operations = Database.getOperations(ds);
		this.lobHandler = lobHandler;
	}

	@Override
	public List<Attachment> getWorkflowAttachments() {
		UserAccount user = UserAccount.getCurrent();
		QueryDefinition query = WorkflowQuery.getViewableObjectsQuery(user.getPayeeId(), WorkflowObject.Type.DOCUMENT);
		QueryBuilder qb = Database.getQueryBuilder();
		List<Integer> ids = this.operations.query(qb.toSql(query), RowMapperUtil.getIntegerMapper());
		if (ids.isEmpty()) {
			return Collections.emptyList();
		} else {
			QueryDefinition workflowQuery = AttachmentQuery.getWorkflowAttachmentQuery(ids);
			return this.operations.query(qb.toSql(workflowQuery), this.workflowDocumentMapper);
		}
	}

	@Override
	public void streamAttachment(String tableName, String idColumn, String idValue, String filename,
			final OutputStream outStream) {
		QueryDefinition query = new QueryDefinition(tableName);
		query.select(tableName, "File");
		query.newConstraint(tableName, idColumn, Constraint.Operator.Equals, idValue);
		query.newConstraint(tableName, "Name", Constraint.Operator.Equals, filename);
		QueryBuilder qb = Database.getQueryBuilder();
		ResultSetExtractor<?> resultSetExtractor = new AbstractLobStreamingResultSetExtractor() {
			@Override
			public void streamData(ResultSet rs) throws SQLException, IOException {
				FileCopyUtils.copy(JdbcAttachmentDaoImpl.this.lobHandler.getBlobAsBinaryStream(rs, 1), outStream);
			}
		};
		this.operations.query(qb.toSql(query), resultSetExtractor);
	}
}
