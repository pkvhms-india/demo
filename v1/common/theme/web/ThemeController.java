package com.varicent.common.theme.web;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.varicent.core.context.VaricentContext;

@Controller
public class ThemeController {
	@RequestMapping({ "/theme/logo.do" })
	public void logo(HttpServletResponse response) throws IOException {
		if (VaricentContext.hasTheme()) {
			FileCopyUtils.copy(VaricentContext.getTheme().getHeaderImage(), response.getOutputStream());
		}

	}
}
