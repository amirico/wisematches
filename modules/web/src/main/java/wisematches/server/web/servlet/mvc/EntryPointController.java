package wisematches.server.web.servlet.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.server.web.servlet.mvc.playground.player.profile.ProfileController;
import wisematches.server.web.servlet.mvc.playground.player.profile.form.PlayerProfileForm;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
public class EntryPointController extends WisematchesController {
	private ProfileController playerProfileController;

	public EntryPointController() {
	}

	@RequestMapping(value = {"/", "/index"})
	public final String mainPage() {
		if (getPrincipal() != null) {
			return "redirect:/playground/scribble/active";
		}
		return "forward:/account/login";
	}

	@RequestMapping("/playground/welcome")
	public String welcomePage(Model model, @ModelAttribute("form") PlayerProfileForm form, Locale locale) {
		playerProfileController.editProfilePage(model, form, locale);
		return "/content/playground/welcome";
	}

	@Autowired
	public void setPlayerProfileController(ProfileController playerProfileController) {
		this.playerProfileController = playerProfileController;
	}
}
