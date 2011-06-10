package wisematches.server.web.controllers.personality;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.server.web.controllers.AbstractPlayerController;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/account/modify")
public class SettingsController extends AbstractPlayerController {
	public SettingsController() {
	}

	@RequestMapping("")
	public String modifyAccountPage(Model model) {
		return "/content/personality/settings/template";
	}

	@ModelAttribute("headerTitle")
	public String getHeaderTitle() {
		return "title.settings";
	}
}
