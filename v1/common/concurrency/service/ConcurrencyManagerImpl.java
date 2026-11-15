package com.varicent.common.concurrency.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varicent.common.concurrency.dao.ConcurrencyDao;

@Service("concurrencyManager")
@Transactional(readOnly = true)
public class ConcurrencyManagerImpl implements ConcurrencyManager {
	private final ConcurrencyDao concurrencyDao;

	@Autowired
	public ConcurrencyManagerImpl(ConcurrencyDao concurrencyDao) {
		this.concurrencyDao = concurrencyDao;
	}

	@Override
	public boolean isGlobalLocked() {
		return this.concurrencyDao.isGlobalLocked();
	}
}
