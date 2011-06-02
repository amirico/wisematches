package wisematches.server.web.controllers.info;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import wisematches.server.web.controllers.AbstractInfoController;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping({"/info"})
public class InformationController extends AbstractInfoController {
	public InformationController() {
		super("classpath:/i18n/server/info/");
	}

	@RequestMapping("/{pageName}")
	public String infoPages(@PathVariable String pageName, Model model, Locale locale, WebRequest webRequest) {
		if (!processInfoPage(pageName, model, locale)) {
			return null;
		}

		if (webRequest.getParameter("plain") != null) {
			return "/content/templates/resources";
		} else {
			return "/content/templates/helpCenter";
		}
	}
}
