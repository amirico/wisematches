package wisematches.server.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/")
public class MainPageController {
	public MainPageController() {
	}

	@RequestMapping("index")
	public String mainPage() {
		return "redirect:/game/dashboard.html";
	}
}
