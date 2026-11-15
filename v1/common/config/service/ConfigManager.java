package com.varicent.common.config.service;

import java.util.Date;
import java.util.Locale;

public interface ConfigManager {
	boolean alertUsersToInquiry();

	boolean alertUsersToSignoff();

	Date getLastCalcTime();

	int getModelVersion();

	int getResultRevision();

	Date getResultTimestamp(int var1);

	boolean isLedgerManualSubmit();

	boolean isModelUnicode();

	boolean webEnabledExists();

	boolean isWebEnabled();

	boolean isPasswordResetEnabled();

	boolean isStrongWebPasswordRequired();

	int getWebPasswordHistory();

	int getWebPasswordExpireDays();

	int getWebPasswordExpireFrequency();

	boolean isWebLockoutEnabled();

	boolean restrictWebFormPayees();

	boolean userLoggingEnabled();

	String getHelpEmail();

	Locale getUserLocale();

	void setUserLocale(Locale var1);
}
