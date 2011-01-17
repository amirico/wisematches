/*
 * Copyright (c) 2011, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

/**
 * TODO: java docs
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/account")
public class AccountController extends AbstractInfoController {
	public AccountController() {
		super("classpath:/i18n/server/account/");
	}

	@RequestMapping("login")
	public String login(Model model, Locale locale) {
		if (!processInfoPage("login", model, locale)) {
			return null;
		}
		model.addAttribute("accountBodyPageName", "login");
		return "/content/account/layout";
	}
}
