package com.varicent.common.attachment.dao;

import java.io.OutputStream;
import java.util.List;

import com.varicent.common.attachment.domain.Attachment;

public interface AttachmentDao {
	List<Attachment> getWorkflowAttachments();

	void streamAttachment(String var1, String var2, String var3, String var4, OutputStream var5);
}
