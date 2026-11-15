package com.varicent.common.tab.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.WebUtils;

import com.varicent.common.tab.domain.Tab;
import com.varicent.common.tab.service.TabManager;

@Controller
public class ExternalUrlController {
	private static final String VIEW = "externalurl/index";
	private TabManager urlManager;

	@Autowired
	public ExternalUrlController(TabManager urlManager) {
		this.urlManager = urlManager;
	}

	@RequestMapping(value = { "/externalurl/index.html" }, method = { RequestMethod.GET })
	public String prepareView(ModelMap model, HttpServletRequest request) {
		Tab tab = this.getTab(request);
		model.addAttribute("title", tab.getName());
		model.addAttribute("url", this.urlManager.getExternalUrl(tab.getId()));
		return "externalurl/index";
	}

	private Tab getTab(HttpServletRequest request) {
		Tab activeTab = (Tab) WebUtils.getSessionAttribute(request, "activeTab");
		if (activeTab != null && activeTab.isExternalURL()) {
			return activeTab;
		} else {
			Assert.state(false, "couldn't get active tab for external url");
			return null;
		}
	}
}
