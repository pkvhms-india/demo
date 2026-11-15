package com.varicent.common.theme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varicent.common.theme.dao.ThemeDao;
import com.varicent.common.theme.domain.Theme;

@Service("themeManager")
@Transactional(readOnly = true)
public class ThemeManagerImpl implements ThemeManager {
	private final ThemeDao themeDao;

	@Autowired
	public ThemeManagerImpl(ThemeDao themeDao) {
		this.themeDao = themeDao;
	}

	@Override
	public Theme getTheme() {
		return this.themeDao.getTheme();
	}
}
