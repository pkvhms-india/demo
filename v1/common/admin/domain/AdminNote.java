package com.varicent.common.admin.domain;

import java.util.Date;

import com.varicent.messages.domain.Message;

public class AdminNote {
	private final int id;
	private final Admin admin;
	private final Date posted;
	private final String subject;
	private final String[] body;
	private final Message.Category category;

	public AdminNote(int id, Admin admin, Date posted, String subject, String body, Message.Category category) {
		this.id = id;
		this.admin = admin;
		this.posted = new Date(posted.getTime());
		this.subject = subject;
		this.body = body.split("\r\n");
		this.category = category;
	}

	public Admin getAdmin() {
		return this.admin;
	}

	public String[] getBody() {
		return this.body;
	}

	public int getId() {
		return this.id;
	}

	public Date getPosted() {
		return (Date) this.posted.clone();
	}

	public String getSubject() {
		return this.subject;
	}

	public Message.Category getCategory() {
		return this.category;
	}
}
