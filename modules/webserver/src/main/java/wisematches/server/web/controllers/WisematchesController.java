package wisematches.server.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.server.security.WMSecurityContext;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
public abstract class WisematchesController {
    protected WisematchesController() {
    }

    @ModelAttribute("principal")
    public Player getPrincipal() {
        return WMSecurityContext.getPrincipal();
    }

    @ModelAttribute("personality")
    public Personality getPersonality() {
        return WMSecurityContext.getPersonality();
    }

    @ModelAttribute("headerTitle")
    public String getHeaderTitle() {
        return "title.playboard";
    }

    @ModelAttribute("requestQueryString")
    public String getRequestQueryString() {
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getQueryString();
    }
}
