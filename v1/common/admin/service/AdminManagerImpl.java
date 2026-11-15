package com.varicent.common.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varicent.common.admin.dao.AdminDao;
import com.varicent.common.admin.domain.AdminNote;

@Service("adminManager")
@Transactional(readOnly = true)
public class AdminManagerImpl implements AdminManager {
	private final AdminDao adminDao;

	@Autowired
	public AdminManagerImpl(AdminDao adminDao) {
		this.adminDao = adminDao;
	}

	@Override
	public AdminNote getAdminNote(int id) {
		return this.adminDao.getAdminNote(id);
	}

	@Override
	public AdminNote getAdminPayeeNote(int id) {
		return this.adminDao.getAdminPayeeNote(id);
	}

	@Override
	public List<AdminNote> getAdminNotes() {
		return this.adminDao.getAdminNotes();
	}

	@Override
	public List<AdminNote> getAdminNotes(int limit) {
		return this.adminDao.getAdminNotes(limit);
	}
}
