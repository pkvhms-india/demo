package com.varicent.common.admin.service;

import java.util.List;

import com.varicent.common.admin.domain.AdminNote;

public interface AdminManager {
	AdminNote getAdminNote(int var1);

	AdminNote getAdminPayeeNote(int var1);

	List<AdminNote> getAdminNotes();

	List<AdminNote> getAdminNotes(int var1);
}
