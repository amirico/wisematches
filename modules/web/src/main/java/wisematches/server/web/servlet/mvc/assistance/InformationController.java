package wisematches.server.web.servlet.mvc.assistance;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.server.web.servlet.mvc.DeprecatedResponse;
import wisematches.server.web.servlet.mvc.WisematchesController;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/info")
@Deprecated
public class InformationController extends WisematchesController {
	public InformationController() {
	}

	@RequestMapping("/{pageName}")
	public String infoPages(@PathVariable String pageName,
							@RequestParam(value = "plain", required = false) String plain,
							Model model, Locale locale) throws InformationUnavailableException {
		if (!staticContentGenerator.generatePage(pageName, "features".equals(pageName), model, locale)) {
			throw new InformationUnavailableException(pageName, plain);
		}

		if (plain != null) {
			return "/content/assistance/static";
		} else {
			return "/content/assistance/help";
		}
	}

	@ResponseBody
	@RequestMapping("/tip.ajax")
	public DeprecatedResponse loadTip(@RequestParam("s") String section, Locale locale) {
		return DeprecatedResponse.success(messageSource.getMessage("game.tip." + section, locale));
	}
}
