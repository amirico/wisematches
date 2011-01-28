package wisematches.server.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
	public AdminController() {
	}

	@RequestMapping("test")
	public String adminTestPage() {
		return "";
	}
}
