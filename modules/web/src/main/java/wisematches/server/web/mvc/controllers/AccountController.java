/*
 * Copyright (c) 2011, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Locale;

/**
 * TODO: java docs
 *
 * @author klimese
 */
@Controller
@RequestMapping("/account")
public class AccountController {
    private InformationController informationController;

    @RequestMapping("unauthorized")
    public String unauthorizedAccess(Model model, Locale locale, WebRequest webRequest) {
        return informationController.infoPages("unauthorized", model, locale, webRequest);
    }

    @Autowired
    public void setInformationController(InformationController informationController) {
        this.informationController = informationController;
    }
}
