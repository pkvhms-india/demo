package com.varicent.common.config.service;

import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varicent.common.config.dao.ConfigDao;
import com.varicent.common.history.dao.HistoryDao;

@Service("configManager")
@Transactional(readOnly = true)
public class ConfigManagerImpl implements ConfigManager {
	private final ConfigDao configDao;
	private final HistoryDao historyDao;
	private Locale locale;

	@Autowired
	public ConfigManagerImpl(ConfigDao configDao, HistoryDao historyDao) {
		this.configDao = configDao;
		this.historyDao = historyDao;
	}

	@Override
	public boolean alertUsersToInquiry() {
		Boolean value = this.configDao.getBooleanValue(ConfigDao.Key.EMAIL_USERS_ON_INQUIRY);
		return value == null ? false : value;
	}

	@Override
	public boolean alertUsersToSignoff() {
		Boolean value = this.configDao.getBooleanValue(ConfigDao.Key.EMAIL_USERS_ON_SIGNOFF);
		return value == null ? false : value;
	}

	@Override
	public Date getLastCalcTime() {
		Integer revision = this.configDao.getIntValue(ConfigDao.Key.COMPUTED_ALL_AT);
		return revision == null ? null : this.historyDao.getRevisionTimestamp(revision);
	}

	@Override
	public int getModelVersion() {
		return this.configDao.getIntValue(ConfigDao.Key.DATABASE_VERSION);
	}

	@Override
	public int getResultRevision() {
		return this.configDao.getIntValue(ConfigDao.Key.RESULT_REVISION);
	}

	@Override
	public Date getResultTimestamp(int revision) {
		return revision == 0 ? null : this.historyDao.getRevisionTimestamp(revision);
	}

	@Override
	public boolean isLedgerManualSubmit() {
		return this.configDao.getBooleanValue(ConfigDao.Key.WEB_LEDGER_MANUAL_SUBMIT);
	}

	@Override
	public boolean isModelUnicode() {
		return this.configDao.isModelUnicode();
	}

	@Override
	public boolean webEnabledExists() {
		return this.configDao.getBooleanValue(ConfigDao.Key.WEB_ENABLED) != null;
	}

	@Override
	public boolean isWebEnabled() {
		Boolean enabled = this.configDao.getBooleanValue(ConfigDao.Key.WEB_ENABLED);
		return enabled == null ? false : enabled;
	}

	@Override
	public boolean isPasswordResetEnabled() {
		Boolean enabled = this.configDao.getBooleanValue(ConfigDao.Key.WEB_PASSWORD_RESET_ENABLED);
		return enabled == null ? false : enabled;
	}

	@Override
	public int getWebPasswordHistory() {
		return this.configDao.getIntValue(ConfigDao.Key.WEB_PASSWORD_HISTORY);
	}

	@Override
	public int getWebPasswordExpireDays() {
		return this.configDao.getIntValue(ConfigDao.Key.WEB_PASSWORD_LIFESPAN);
	}

	@Override
	public int getWebPasswordExpireFrequency() {
		return this.configDao.getIntValue(ConfigDao.Key.WEB_PASSWORD_EXPIRE_PROMPT_FREQUENCY);
	}

	@Override
	public boolean isStrongWebPasswordRequired() {
		Boolean value = this.configDao.getBooleanValue(ConfigDao.Key.STRONG_WEB_PASSWORDS);
		return value == null ? false : value;
	}

	@Override
	public boolean isWebLockoutEnabled() {
		Boolean lockoutEnabled = this.configDao.getBooleanValue(ConfigDao.Key.WEB_LOCKOUT_ENABLED);
		return lockoutEnabled == null ? false : lockoutEnabled;
	}

	@Override
	public boolean restrictWebFormPayees() {
		return this.configDao.getBooleanValue(ConfigDao.Key.WEB_FORM_RESTRICT_PAYEES);
	}

	@Override
	public boolean userLoggingEnabled() {
		return this.configDao.getBooleanValue(ConfigDao.Key.WEB_USER_LOGGING);
	}

	@Override
	public String getHelpEmail() {
		return this.configDao.getValue(ConfigDao.Key.WEB_HELP_EMAIL);
	}

	@Override
	public Locale getUserLocale() {
		return this.locale;
	}

	@Override
	public void setUserLocale(Locale locale) {
		this.locale = locale;
	}
}
