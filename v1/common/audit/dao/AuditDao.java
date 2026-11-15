package com.varicent.common.audit.dao;

import com.varicent.core.i18n.Message;
import com.varicent.security.domain.UserAccount;

public interface AuditDao {
	void log(Message var1, Message var2, Message var3);

	void log(Message var1, Message var2, Message var3, UserAccount var4);

	void log(Message var1, Message var2, Message var3, String var4);

	void logRevision(Message var1, Message var2, Message var3);
}
