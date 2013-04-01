package wisematches.server.web.servlet.mvc.playground.awards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.server.services.award.*;
import wisematches.server.web.servlet.mvc.UnknownEntityException;
import wisematches.server.web.servlet.mvc.WisematchesController;

import java.util.EnumSet;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/award")
public class AwardsController extends WisematchesController {
	private AwardsManager awardsManager;

	public AwardsController() {
	}

	@RequestMapping(value = "view")
	public String viewPage(@RequestParam(value = "c") Integer c, @RequestParam(value = "w", required = false) String w, Model model) {
		final AwardDescriptor descriptor = awardsManager.getAwardDescriptor(c);
		if (descriptor == null) {
			throw new UnknownEntityException(w, "award.code");
		}

		AwardWeight weight = null;
		try {
			if (w != null) {
				weight = AwardWeight.valueOf(w.toUpperCase());
			}
		} catch (IllegalArgumentException e) {
			throw new UnknownEntityException(w, "award.weight");
		}

		final List<Award> awards = awardsManager.searchEntities(null, new AwardContext(descriptor, weight != null ? EnumSet.of(weight) : null), null, null);
		model.addAttribute("awards", awards);
		model.addAttribute("weight", weight);
		model.addAttribute("descriptor", descriptor);
		return "/content/playground/award/view";
	}

	@Autowired
	public void setAwardsManager(AwardsManager awardsManager) {
		this.awardsManager = awardsManager;
	}
}
