package wisematches.server.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.server.security.WMAuthorities;

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
		if (WMAuthorities.USER.isAuthorityGranted()) {
			return "redirect:/game/dashboard.html";
		}
		return "redirect:/account/login.html";
	}
}
