package wisematches.server.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.playground.message.MessageManager;
import wisematches.server.security.WMSecurityContext;

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
}
