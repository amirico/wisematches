package wisematches.server.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

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
			return "/content/info/modelConverter";
		} else {
			return "/content/info/layout";
		}
	}
}
