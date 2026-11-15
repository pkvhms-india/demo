package com.varicent.common.admin.dao;

import java.util.List;

import com.varicent.common.admin.domain.AdminNote;

public interface AdminDao {
	AdminNote getAdminNote(int var1);

	AdminNote getAdminPayeeNote(int var1);

	List<AdminNote> getAdminNotes();

	List<AdminNote> getAdminNotes(int var1);

	void Insert(String var1, String var2, String var3);

	void Insert(String var1, String var2, String var3, String var4);

	int getMaxAdminNoteId();

	void InsertVariable(int var1, String var2, String var3);
}
