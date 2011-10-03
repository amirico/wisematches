package wisematches.server.web.controllers.info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.server.web.controllers.AbstractInfoController;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.i18n.GameMessageSource;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping({"/info"})
public class InformationController extends AbstractInfoController {
	private GameMessageSource messageSource;

	public InformationController() {
		super("classpath:/i18n/info/");
	}

	@RequestMapping("/{pageName}")
	public String infoPages(@PathVariable String pageName,
							@RequestParam(value = "plain", required = false) String plain,
							Model model, Locale locale) {
		if (!processInfoPage(pageName, model, locale)) {
			return null;
		}

		if (plain != null) {
			return "/content/templates/resources";
		} else {
			return "/content/templates/helpCenter";
		}
	}

	@ResponseBody
	@RequestMapping("/tip.ajax")
	public ServiceResponse loadTip(@RequestParam("s") String section, Locale locale) {
		return ServiceResponse.success(messageSource.getMessage("game.tip." + section, locale));
	}

	@Autowired
	public void setMessageSource(GameMessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
