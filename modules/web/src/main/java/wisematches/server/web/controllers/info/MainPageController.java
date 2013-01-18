package wisematches.server.web.controllers.info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.server.security.WMAuthorities;
import wisematches.server.web.controllers.WisematchesController;
import wisematches.server.web.controllers.personality.profile.PlayerProfileController;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/")
public class MainPageController extends WisematchesController {
	private PlayerProfileController playerProfileController;

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
	public String welcomePage(Model model, Locale locale) {
		playerProfileController.editProfile(model, locale);
		return "/content/playground/welcome";
	}

	@RequestMapping("index")
	public String mainPage2() {
		return mainPage();
	}

	@Autowired
	public void setPlayerProfileController(PlayerProfileController playerProfileController) {
		this.playerProfileController = playerProfileController;
	}
}
