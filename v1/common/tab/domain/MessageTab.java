package com.varicent.common.tab.domain;

public class MessageTab {
	public static final MessageTab INBOX = new MessageTab("messagecenter.sectionLinks.inbox", "/messages/index.html",
			"/images/inbox.png");
	public static final MessageTab ADMIN = new MessageTab("messagecenter.sectionLinks.adminMessages",
			"/messages/messages.html", "/images/message.png");
	public static final MessageTab DOCUMENTS = new MessageTab("messagecenter.sectionLinks.documents",
			"/documents/index.html", "/images/document.png");
	public static final MessageTab TASKS = new MessageTab("messagecenter.sectionLinks.signoff", "/workflow/index.html",
			"/images/task.png");
	public static final MessageTab INQUIRIES = new MessageTab("messagecenter.sectionLinks.inquiries",
			"/inquiries/index.html", "/images/inquiry.png");
	public static final MessageTab BPM = new MessageTab("messagecenter.sectionLinks.bpm", "/bpm/index.html",
			"/images/bpm.png");
	private final String id;
	private final String url;
	private final String imageUrl;
	private int count;

	public MessageTab(String id, String url, String imageUrl) {
		this.id = id;
		this.url = url;
		this.imageUrl = imageUrl;
		this.count = 0;
	}

	public String getId() {
		return this.id;
	}

	public String getUrl() {
		return this.url;
	}

	public String getImageUrl() {
		return this.imageUrl;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int value) {
		this.count = value;
	}
}
