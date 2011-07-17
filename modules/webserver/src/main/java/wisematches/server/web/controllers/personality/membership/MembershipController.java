package wisematches.server.web.controllers.personality.membership;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.personality.Membership;
import wisematches.playground.restriction.RestrictionManager;
import wisematches.server.web.controllers.AbstractInfoController;

import java.util.Arrays;

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
        model.addAttribute("memberships", Arrays.asList(Membership.GUEST, Membership.BASIC, Membership.SILVER, Membership.GOLD, Membership.PLATINUM));
        model.addAttribute("restrictionDescriptions", restrictionManager.getRestrictionDescriptions());
        return "/content/personality/account/membership/view";
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
