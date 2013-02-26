package wisematches.server.web.servlet.mvc.playground;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.server.web.servlet.mvc.UnknownEntityException;
import wisematches.server.web.servlet.mvc.WisematchesController;
import wisematches.server.web.servlet.mvc.playground.player.profile.ProfileController;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/")
@Deprecated
public class WelcomePageController extends WisematchesController {
	private ProfileController playerProfileController;

	public WelcomePageController() {
	}

	@RequestMapping("/playground/welcome")
	public String welcomePage(Model model, Locale locale) throws UnknownEntityException {
		playerProfileController.editProfile(model, locale);
		return "/content/playground/welcome";
	}

	@Autowired
	public void setPlayerProfileController(ProfileController playerProfileController) {
		this.playerProfileController = playerProfileController;
	}
}
