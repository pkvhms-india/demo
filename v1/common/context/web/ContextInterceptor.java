package com.varicent.common.context.web;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import com.varicent.common.config.service.ConfigManager;
import com.varicent.common.context.web.util.AuthorityUtilExt;
import com.varicent.common.tab.domain.MessageTab;
import com.varicent.common.tab.domain.Tab;
import com.varicent.common.tab.domain.TabGroup;
import com.varicent.common.tab.service.TabManager;
import com.varicent.common.theme.domain.Theme;
import com.varicent.core.context.VaricentContext;
import com.varicent.core.exceptions.ContextInterceptorSessionCreationFailure;
import com.varicent.messages.service.MessagesManager;
import com.varicent.security.domain.IFrameConfig;
import com.varicent.security.domain.UserAccount;
import com.varicent.status.service.StatusManager;

@Component
public class ContextInterceptor extends HandlerInterceptorAdapter {
	private static final String WEB_ENABLED_ATTRIBUTE = "webEnabled";
	private static final String APP_VERSION_ATTRIBUTE = "appVersion";
	public static final String ADMIN_ATTRIBUTE = "adminEmail";
	private static final String THEME_ATTRIBUTE = "theme";
	private static final String LOGO_SIZE_ATTRIBUTE = "logo";
	private static final String LAST_LOGIN_ATTRIBUTE = "lastLogin";
	private static final String NEW_MESSAGES_ATTRIBUTE = "newMessages";
	private static final String ACTIVE_TAB_ATTRIBUTE = "activeTab";
	private static final String SELECTED_TAB_ATTRIBUTE = "selectedTab";
	private static final String SELECTED_SUB_TAB_ATTRIBUTE = "selectedSubTab";
	private static final String TABS_ATTRIBUTE = "tabs";
	private static final String MESSAGE_CENTRE_ATTRIBUTE = "showMessageCentre";
	private static final String MSG_TABS_ATTRIBUTE = "msgTabs";
	private static final String SELECTED_MSG_TAB_ATTRIBUTE = "selectedMsgTab";
	private static final String SHOW_BATCH_EXPORT_TO_PDF = "showBatchExportToPdf";
	private static final String SHOW_EXPORT_TO_PDF = "showExportToPdf";
	private static final String SHOW_INQUIRY = "showInquiry";
	private static final String PATH_INBOX = "/messages/index.html";
	private static final String PATH_MESSAGES = "/messages/";
	private static final String PATH_DOCUMENTS = "/documents/";
	private static final String PATH_INQUIRIES = "/inquiries/";
	private static final String PATH_TASKS = "/workflow/";
	private static final String PATH_BPM = "/bpm/";
	private static final double LOGO_FY_COEFFICIENT_A = 410.0F;
	private static final double LOGO_FY_COEFFICIENT_B = 6.25F;
	private static final String SAVED_REQUEST_KEY = "SPRING_SECURITY_SAVED_REQUEST_KEY";
	private final MessagesManager messagesManager;
	private final TabManager tabManager;
	private final StatusManager statusManager;
	private final ConfigManager configManager;
	private final CachingContextInterceptor cachingContextInterceptor;

	@Autowired
	public ContextInterceptor(MessagesManager messagesManager, TabManager tabManager, StatusManager statusManager,
			ConfigManager configManager, CachingContextInterceptor cachingContextInterceptor) {
		this.messagesManager = messagesManager;
		this.tabManager = tabManager;
		this.statusManager = statusManager;
		this.configManager = configManager;
		this.cachingContextInterceptor = cachingContextInterceptor;
	}

	private Tab getTab(List<Tab> tabs, String tabIdParameter) {
		if (tabIdParameter != null && tabIdParameter.length() != 0) {
			int tabId;
			try {
				tabId = Integer.parseInt(tabIdParameter);
			} catch (NumberFormatException var5) {
				return null;
			}

			return getTab(tabs, tabId);
		} else {
			return null;
		}
	}

	private static Tab getTab(List<Tab> tabs, int tabId) {
		for (Tab tab : tabs) {
			if (tab.getId() == tabId) {
				return tab;
			}

			if (tab.isTabGroup()) {
				TabGroup tabGroup = (TabGroup) tab;
				List<Tab> subTabs = tabGroup.getSubTabs();
				Tab subTab = getTab(subTabs, tabId);
				if (subTab != null) {
					return subTab;
				}
			}
		}

		return null;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav)
			throws Exception {
		Theme theme = VaricentContext.getTheme();
		if (!IFrameConfig.iFrameEnabled()) {
			response.addHeader("X-FRAME-OPTIONS", "SAMEORIGIN");
		}

		request.setAttribute("webEnabled", this.configManager.webEnabledExists() && this.configManager.isWebEnabled());
		request.setAttribute("adminEmail", VaricentContext.getAdminEmail());
		request.setAttribute("theme", theme);
		this.cachingContextInterceptor.setCachingResponseHeaders(request, response);
		String url = request.getRequestURL().toString();
		if (AuthorityUtilExt.userHasAuthority("ROLE_USER")
				&& (url.endsWith("inquiries/view.do") || !url.endsWith(".do"))) {
			request.setAttribute("newMessages", this.messagesManager.getNewMessageCount());
			request.setAttribute("lastLogin", UserAccount.getCurrent().getLastLogin());
		}

		if (theme != null && theme.getThemeType().equals(Theme.CustomThemeType.Standard) && theme.getEnabled()
				&& theme.getHeaderImage() != null) {
			request.setAttribute("logo", this.getLogoImageSize());
		}

		this.setSelectedTabsInRequest(request);

		try {
			this.setPdfAndInquiryIconVisiblity(request);
		} catch (IllegalStateException exception) {
			throw new ContextInterceptorSessionCreationFailure(exception);
		}
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (AuthorityUtilExt.userHasAuthority("ROLE_USER")) {
			if (WebUtils.getSessionAttribute(request, "tabs") == null) {
				WebUtils.setSessionAttribute(request, "tabs", this.tabManager.getTabs());
			}

			WebUtils.setSessionAttribute(request, "msgTabs", this.tabManager.getMessageTabs());
		}

		this.setSelectedTabsInRequest(request);
		this.clearPdfAndInquiryIconvisiblity(request);
		return super.preHandle(request, response, handler);
	}

	private void setSelectedTabsInRequest(HttpServletRequest request) {
		Tab activeTab = null;
		Tab tab = null;
		Tab subTab = null;
		MessageTab selectedMsgTab = this.getRequestedMessageTab(request);
		if (selectedMsgTab == null) {
			List<Tab> tabs = (List) WebUtils.getSessionAttribute(request, "tabs");
			if (tabs == null || tabs.isEmpty()) {
				return;
			}

			String tabId = request.getParameter("tabId");
			if (tabId == null) {
				activeTab = (Tab) WebUtils.getSessionAttribute(request, "activeTab");
				if (activeTab != null) {
					tabId = String.valueOf(activeTab.getId());
				}
			}

			tab = this.getTab(tabs, tabId);
			subTab = null;
			if (tab != null && tab.isSubTab()) {
				tabId = String.valueOf(tab.getParentId());
				subTab = tab;
				tab = this.getTab(tabs, tabId);
			}

			if (subTab == null && tab == null) {
				SavedRequest savedRequest = (SavedRequest) WebUtils.getSessionAttribute(request,
						"SPRING_SECURITY_SAVED_REQUEST_KEY");
				if (savedRequest != null) {
					WebUtils.setSessionAttribute(request, "SPRING_SECURITY_SAVED_REQUEST_KEY", (Object) null);
				}
			}

			activeTab = null;
			if (subTab != null) {
				activeTab = subTab;
			} else if (tab != null) {
				activeTab = tab;
			}
		}

		WebUtils.setSessionAttribute(request, "activeTab", activeTab);
		request.setAttribute("appVersion", this.statusManager.getStatus().getApplicationVersion());
		request.setAttribute("selectedTab", tab);
		request.setAttribute("selectedSubTab", subTab);
		request.setAttribute("showMessageCentre", selectedMsgTab != null);
		request.setAttribute("selectedMsgTab", selectedMsgTab);
	}

	private void setPdfAndInquiryIconVisiblity(HttpServletRequest request) {
		Boolean showBatchExportToPDF = (Boolean) WebUtils.getSessionAttribute(request, "showBatchExportToPdf");
		if (showBatchExportToPDF == null) {
			WebUtils.setSessionAttribute(request, "showBatchExportToPdf", false);
		}

		Boolean showExportToPDF = (Boolean) WebUtils.getSessionAttribute(request, "showExportToPdf");
		if (showExportToPDF == null) {
			WebUtils.setSessionAttribute(request, "showExportToPdf", false);
		}

		Boolean showInquiry = (Boolean) WebUtils.getSessionAttribute(request, "showInquiry");
		if (showInquiry == null) {
			WebUtils.setSessionAttribute(request, "showInquiry", false);
		}

	}

	private void clearPdfAndInquiryIconvisiblity(HttpServletRequest request) {
		WebUtils.setSessionAttribute(request, "showBatchExportToPdf", (Object) null);
		WebUtils.setSessionAttribute(request, "showExportToPdf", (Object) null);
		WebUtils.setSessionAttribute(request, "showInquiry", (Object) null);
	}

	private MessageTab getRequestedMessageTab(HttpServletRequest request) {
		String path = request.getServletPath();
		String tabId = request.getParameter("tabId");
		if (path.startsWith("/messages/index.html")) {
			return MessageTab.INBOX;
		} else if (path.startsWith("/messages/")) {
			return MessageTab.ADMIN;
		} else if (path.startsWith("/inquiries/") && tabId == null) {
			return MessageTab.INQUIRIES;
		} else if (path.startsWith("/documents/")) {
			return MessageTab.DOCUMENTS;
		} else if (path.startsWith("/workflow/")) {
			return MessageTab.TASKS;
		} else {
			return path.startsWith("/bpm/") ? MessageTab.BPM : null;
		}
	}

	private Dimension getLogoImageSize() {
		Dimension size;
		try {
			Theme theme = VaricentContext.getTheme();
			InputStream stream = new ByteArrayInputStream(theme.getHeaderImage());
			BufferedImage image = ImageIO.read(stream);
			double width = image.getWidth();
			double height = image.getHeight();
			double ratio = width / height;
			int newHeight = (int) Math.round(410.0F / (ratio + 6.25F));
			int newWidth = (int) Math.round(ratio * newHeight);
			size = new Dimension(Math.min(image.getWidth(), newWidth), Math.min(image.getHeight(), newHeight));
		} catch (IOException var13) {
			size = new Dimension(0, 0);
		}

		return size;
	}
}
