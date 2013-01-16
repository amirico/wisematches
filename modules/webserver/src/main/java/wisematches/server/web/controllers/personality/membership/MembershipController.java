package wisematches.server.web.controllers.personality.membership;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.playground.restriction.RestrictionManager;
import wisematches.server.web.controllers.AbstractInfoController;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/account/membership")
public class MembershipController extends AbstractInfoController {
	private RestrictionManager restrictionManager;

	public MembershipController() {
	}

	@RequestMapping("")
	public String viewMembership(Model model) {
		return viewMembershipPages(model);
	}

	@RequestMapping("view")
	public String viewMembershipPages(Model model) {
		model.addAttribute("restrictionManager", restrictionManager);
		return "/content/personality/membership/view";
	}

	@ModelAttribute("headerTitle")
	public String getMembershipTitle() {
		return "title.membership";
	}

	@Autowired
	public void setRestrictionManager(RestrictionManager restrictionManager) {
		this.restrictionManager = restrictionManager;
	}
}
