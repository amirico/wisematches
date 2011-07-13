package wisematches.server.web.controllers.playground;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.server.web.controllers.AbstractPlayerController;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/ignores")
public class BlacklistController extends AbstractPlayerController {
	public BlacklistController() {
	}

	@RequestMapping("view")
	public String viewIgnoresList() {
		return "/content/playground/ignores/view";
	}
}
