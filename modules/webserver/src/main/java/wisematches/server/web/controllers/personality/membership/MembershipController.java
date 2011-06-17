package wisematches.server.web.controllers.personality.membership;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.server.web.controllers.AbstractInfoController;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/account/membership")
public class MembershipController extends AbstractInfoController {
	public MembershipController() {
	}

	@RequestMapping("")
	public String viewMembership(Model model) {
		return viewMembershipPages(model);
	}

	@RequestMapping("view")
	public String viewMembershipPages(Model model) {
		return "/content/personality/account/membership/view";
	}

	@ModelAttribute("headerTitle")
	public String getMembershipTitle() {
		return "title.membership";
	}
}
