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
import org.springframework.web.context.request.WebRequest;
import wisematches.server.web.spring.i18n.FreeMarkerNodeModelResourceBundle;

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

	@RequestMapping("/{pageName}")
	public String infoPages(@PathVariable String pageName, Model model, Locale locale, WebRequest webRequest) {
		if (!handleInfoPage(pageName, model, locale, webRequest)) {
			return null;
		}
		if (webRequest.getParameter("plain") != null) {
			return "/content/common/information";
		} else {
			return "/content/login/layout";
		}
	}

	private boolean handleInfoPage(String pageName, Model model, Locale locale, WebRequest webRequest) {
		final NodeModel informationHolder = freeMarkerNodeModelResourceBundle.getInfoHolder(pageName, locale);
		if (informationHolder == null) {
			return false;
		}

		model.addAttribute("id", pageName);
		model.addAttribute("pageName", "/content/common/information.ftl");
		model.addAttribute("informationHolder", informationHolder);
		return true;
	}

	@Autowired(required = true)
	public void setFreeMarkerNodeModelResourceBundle(FreeMarkerNodeModelResourceBundle freeMarkerNodeModelResourceBundle) {
		this.freeMarkerNodeModelResourceBundle = freeMarkerNodeModelResourceBundle;
	}
}