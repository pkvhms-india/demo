package com.varicent.common.audit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varicent.common.audit.dao.AuditDao;
import com.varicent.core.i18n.Message;

@Service("auditManager")
@Transactional(readOnly = true)
public class AuditManagerImpl implements AuditManager {
	private final AuditDao auditDao;

	@Autowired
	public AuditManagerImpl(AuditDao auditDao) {
		this.auditDao = auditDao;
	}

	@Override
	public void log(Message module, Message event, Message message) {
		this.auditDao.log(module, event, message);
	}
}
