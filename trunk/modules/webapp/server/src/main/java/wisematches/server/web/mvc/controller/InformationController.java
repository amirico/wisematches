/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.mvc.controller;

import freemarker.ext.dom.NodeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import wisematches.server.web.spring.i18n.FreeMarkerNodeModelResourceBundle;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * This is base controller that processes any requests in <i>/info/*</i>.
 *
 * @author klimese
 */
@Controller
@RequestMapping({"/", "/info"})
public class InformationController {
	private FreeMarkerNodeModelResourceBundle freeMarkerNodeModelResourceBundle;

	@RequestMapping("/plain/{pageName}")
	public String infoPlainPages(@PathVariable String pageName, Model model, Locale locale, HttpServletRequest request)
			throws NoSuchRequestHandlingMethodException {
		handleInfoPage(pageName, model, locale, request);
		return "/content/common/information";
	}

	@RequestMapping("/{pageName}")
	public String infoPages(@PathVariable String pageName, Model model, Locale locale, HttpServletRequest request)
			throws NoSuchRequestHandlingMethodException {
		handleInfoPage(pageName, model, locale, request);
		return "/content/login/layout";
	}

	private void handleInfoPage(String pageName, Model model, Locale locale, HttpServletRequest request) throws NoSuchRequestHandlingMethodException {
		final NodeModel informationHolder = freeMarkerNodeModelResourceBundle.getInfoHolder(pageName, locale);
		if (informationHolder == null) {
			throw new NoSuchRequestHandlingMethodException(request);
		}

		model.addAttribute("id", pageName);
		model.addAttribute("pageName", "/content/common/information.ftl");
		model.addAttribute("informationHolder", informationHolder);
	}

	@Autowired(required = true)
	public void setFreeMarkerNodeModelResourceBundle(FreeMarkerNodeModelResourceBundle freeMarkerNodeModelResourceBundle) {
		this.freeMarkerNodeModelResourceBundle = freeMarkerNodeModelResourceBundle;
	}
}