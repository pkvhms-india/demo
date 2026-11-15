package com.varicent.common.messages.service;

import com.varicent.core.util.MessageNavigation;
import com.varicent.messages.domain.Message;

public interface NavigationManager {
	MessageNavigation getNavigationMessages(int var1, Message.Category var2, MessageNavigation.ListType var3);

	MessageNavigation getInquiryNavigationMessages(int var1, int var2, Message.Category var3,
			MessageNavigation.SubType var4, int var5);
}
