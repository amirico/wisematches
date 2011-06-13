package wisematches.server.web.controllers.personality.settings;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import wisematches.personality.player.Player;
import wisematches.server.web.controllers.AbstractPlayerController;
import wisematches.server.web.controllers.personality.settings.form.SettingsForm;
import wisematches.server.web.controllers.personality.settings.form.TimeZonesInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/account/modify")
public class SettingsController extends AbstractPlayerController {
    public SettingsController() {
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String modifyAccountPage(Model model,
                                    @ModelAttribute("settings")
                                    SettingsForm form) {
        final Player principal = getPrincipal();
        if (principal.getTimeZone() != null) {
            form.setTimezone(principal.getTimeZone().getDisplayName());
        }
        form.setLanguage(principal.getLanguage().name().toLowerCase());
        model.addAttribute("timeZones", TimeZonesInfo.getTimeZones());
        return "/content/personality/settings/template";
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String modifyAccountAction(HttpServletRequest request, HttpServletResponse response,
                                      @Valid @ModelAttribute("settings") SettingsForm form,
                                      BindingResult result, Model model, SessionStatus status) {
        return modifyAccountPage(model, form);
    }

    @ModelAttribute("headerTitle")
    public String getHeaderTitle() {
        return "title.settings";
    }
}
