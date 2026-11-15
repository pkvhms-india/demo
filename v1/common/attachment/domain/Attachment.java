package com.varicent.common.attachment.domain;

import java.util.Date;

public class Attachment {
	private final String tableName;
	private final String idColumn;
	private final String idValue;
	private final String fileName;
	private final Date date;
	private final FileType fileType;

	public Attachment(String tableName, String idColumn, String idValue, String fileName, FileType fileType,
			Date date) {
		this.tableName = tableName;
		this.idColumn = idColumn;
		this.idValue = idValue;
		this.fileName = fileName;
		this.fileType = fileType;
		this.date = date;
	}

	public String getTable() {
		return this.tableName;
	}

	public String getIDColumn() {
		return this.idColumn;
	}

	public String getFileId() {
		return this.idValue;
	}

	public String getName() {
		return this.fileName;
	}

	public FileType getFileType() {
		return this.fileType;
	}

	public Date getDate() {
		return this.date;
	}

	public static enum FileType {
		OTHER, DOC, XLS, PPT, PDF;

		public static FileType toFileType(int i) {
			switch (i) {
			case 1:
				return DOC;
			case 2:
				return XLS;
			case 3:
				return PPT;
			case 4:
				return PDF;
			default:
				return OTHER;
			}
		}
	}
}
