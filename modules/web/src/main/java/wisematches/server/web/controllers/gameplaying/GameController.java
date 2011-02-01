/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.controllers.gameplaying;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/game")
public class GameController {
	@RequestMapping("/{pageName}")
	public String gamePages(@PathVariable String pageName, Model model, Locale locale, WebRequest webRequest) {


		model.addAttribute("pageName", pageName);
		return "/content/game/layout";
	}
}
