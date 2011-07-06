package wisematches.server.web.controllers.playground;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.server.web.controllers.AbstractPlayerController;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/players")
public class PlayersController extends AbstractPlayerController {
	public PlayersController() {
	}

	@RequestMapping("ignores")
	public String showIgnores() {
		return "/content/playground/players/ignores";
	}
}
