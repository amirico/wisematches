/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author klimese
 */
@Controller
@RequestMapping({"/", "/info"})
public class InformationController {
	@RequestMapping("/{pageName}")
	public String infoPages(@PathVariable String pageName, Model model) {
		model.addAttribute("pageName", "/content/common/info/" + pageName + ".ftl");
		return "/content/login/layout";
	}

	@RequestMapping("/index")
	public String mainPage(Model model) {
		model.addAttribute("pageName", "/content/login/panel/index.ftl");
		return "/content/login/layout";
	}
}