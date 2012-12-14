package wisematches.server.web.controllers.info;

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

	@RequestMapping("")
	public String mainPage() {
		if (WMAuthorities.USER.isAuthorityGranted()) {
			return "redirect:/playground/scribble/active";
		}
		return "forward:/account/login";
	}

	@RequestMapping("/playground/welcome")
	public String welcomePage() {
		return "forward:/playground/profile/edit?social=true";
	}

	@RequestMapping("index")
	public String mainPage2() {
		return mainPage();
	}
}
