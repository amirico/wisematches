package wisematches.server.web.controllers.account;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/account/membership")
public class MembershipController {
	public MembershipController() {
	}

	@RequestMapping("view")
	public String viewMembershipPages(Model model) {
		return "/content/account/pages/membership/view.ftl";
	}

	@ModelAttribute("headerTitle")
	public String getMembershipTitle() {
		return "title.membership";
	}
}
