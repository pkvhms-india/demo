package com.varicent.common.config.dao;

public interface ConfigDao {
	Boolean getBooleanValue(Key var1);

	Integer getIntValue(Key var1);

	String getValue(Key var1);

	boolean isModelUnicode();

	public static enum Key {
		CHANGE_WEB_PASSWORD_ON_FIRST_LOGIN("ChangeWebPasswordOnFirstLogin"), COMPUTED_ALL_AT("ComputedAllAt"),
		DATABASE_VERSION("DatabaseVersion"), EMAIL_USERS_ON_INQUIRY("EmailUsersOnInquiry"),
		EMAIL_USERS_ON_SIGNOFF("EmailUsersOnSignOff"), ENABLE_PENDING_WORKFLOW("EnablePendingWorkflow"),
		FAILED_WEB_LOG_ON_ATTEMPTS("FailedWebLogOnAttempts"), RESULT_REVISION("ResultRevision"),
		STRONG_WEB_PASSWORDS("StrongWebPasswords"), WEB_ENABLED("WebEnabled"),
		WEB_FORM_RESTRICT_PAYEES("WebFormRestrictPayees"), WEB_HELP_EMAIL("WebHelpEmail"),
		WEB_LEDGER_HEADER_INTERVAL("WebLedgerHeaderInterval"), WEB_LEDGER_MANUAL_SUBMIT("WebLedgerManualSubmit"),
		WEB_LOCKOUT_ENABLED("WebLockoutEnabled"),
		WEB_PASSWORD_EXPIRE_PROMPT_DAYS_BEFORE("WebPasswordExpirePromptDaysBefore"),
		WEB_PASSWORD_EXPIRE_PROMPT_FREQUENCY("WebPasswordExpirePromptFrequency"),
		WEB_PASSWORD_LIFESPAN("WebPasswordLifespan"), WEB_PASSWORD_RESET_ENABLED("WebPasswordResetEnabled"),
		WEB_PASSWORD_RESET_EXPIRE_SECONDS("WebPasswordResetExpireSeconds"), WEB_PASSWORD_HISTORY("WebPasswordHistory"),
		WEB_USER_LOGGING("WebUserLogging"), WEB_BASE_URL("WebBaseURL");

		private final String key;

		private Key(String key) {
			this.key = key;
		}

		@Override
		public String toString() {
			return this.key;
		}
	}
}
