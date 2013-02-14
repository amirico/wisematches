package wisematches.server.web.servlet.mvc.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.playground.restriction.RestrictionManager;
import wisematches.server.web.servlet.mvc.WisematchesController;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/account/membership")
public class MembershipController extends WisematchesController {
	private RestrictionManager restrictionManager;

	public MembershipController() {
		super("title.membership");
	}

	@RequestMapping("")
	public String viewMembership(Model model) {
		return viewMembershipPages(model);
	}

	@RequestMapping("view")
	public String viewMembershipPages(Model model) {
		model.addAttribute("restrictionManager", restrictionManager);
		return "/content/account/membership/view";
	}

	@Autowired
	public void setRestrictionManager(RestrictionManager restrictionManager) {
		this.restrictionManager = restrictionManager;
	}
}
