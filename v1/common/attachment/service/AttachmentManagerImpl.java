package com.varicent.common.attachment.service;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varicent.common.attachment.dao.AttachmentDao;
import com.varicent.common.attachment.domain.Attachment;

@Service("attachmentManager")
@Transactional(readOnly = true)
public class AttachmentManagerImpl implements AttachmentManager {
	private final AttachmentDao attachmentDao;

	@Autowired
	public AttachmentManagerImpl(AttachmentDao attachmentDao) {
		this.attachmentDao = attachmentDao;
	}

	@Override
	public int getAttachmentCount() {
		return this.getAttachments().size();
	}

	@Override
	public List<Attachment> getAttachments() {
		List<Attachment> attachments = new ArrayList();
		attachments.addAll(this.attachmentDao.getWorkflowAttachments());
		return attachments;
	}

	@Override
	public void streamAttachment(String tableName, String idColumn, String idValue, String filename,
			OutputStream outStream) {
		this.attachmentDao.streamAttachment(tableName, idColumn, idValue, filename, outStream);
	}
}
