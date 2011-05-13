package wisematches.server.web.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/admin")
public class AdministrationController {
	public AdministrationController() {
	}

	@RequestMapping("test")
	public String adminTestPage() {
		return "";
	}
}
