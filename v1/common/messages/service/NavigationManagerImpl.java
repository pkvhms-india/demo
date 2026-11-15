package com.varicent.common.messages.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varicent.core.util.MessageNavigation;
import com.varicent.messages.dao.MessagesDao;
import com.varicent.messages.domain.Message;

@Service("navigationManager")
@Transactional(readOnly = true)
public class NavigationManagerImpl implements NavigationManager {
	private static final int PAGINATION_SIZE = 5;
	private final MessagesDao messagesDao;

	@Autowired
	public NavigationManagerImpl(MessagesDao messagesDao) {
		this.messagesDao = messagesDao;
	}

	@Override
	public MessageNavigation getNavigationMessages(int messageId, Message.Category category,
			MessageNavigation.ListType listType) {
		Message message = this.messagesDao.getMessage(messageId, category, listType, false);
		Message nextOlder = this.messagesDao.getMessageNext(message.getDate(), messageId, listType,
				MessagesDao.NextDirection.DOWN);
		Message nextNewer = this.messagesDao.getMessageNext(message.getDate(), messageId, listType,
				MessagesDao.NextDirection.UP);
		return new MessageNavigation(nextOlder, nextNewer, listType);
	}

	@Override
	public MessageNavigation getInquiryNavigationMessages(int messageId, int messageStatus, Message.Category category,
			MessageNavigation.SubType subType, int status) {
		Message message = this.messagesDao.getMessage(messageId, category, MessageNavigation.ListType.INQUIRY, true);
		Message nextOlder = this.messagesDao.getInquiryMessageNext(message.getDate(), messageId, messageStatus, subType,
				status, MessagesDao.NextDirection.DOWN);
		Message nextNewer = this.messagesDao.getInquiryMessageNext(message.getDate(), messageId, messageStatus, subType,
				status, MessagesDao.NextDirection.UP);
		return new MessageNavigation(nextOlder, nextNewer, MessageNavigation.ListType.INQUIRY);
	}
}
