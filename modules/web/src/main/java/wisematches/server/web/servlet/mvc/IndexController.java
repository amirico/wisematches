package wisematches.server.web.servlet.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
public class IndexController extends WisematchesController {
	public IndexController() {
	}

	@RequestMapping(value = {"/", "/index"})
	public final String mainPage() {
		if (getPersonality() != null) {
			return "redirect:/playground/scribble/active";
		}
		return "forward:/account/login";
	}
}
