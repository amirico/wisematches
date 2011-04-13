package wisematches.server.web.controllers.gameplaying;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.server.web.controllers.AbstractPlayerController;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/game")
public class PlayerProfileController extends AbstractPlayerController {
    public PlayerProfileController() {
    }

    @RequestMapping("profile")
    public String createGamePage(@ModelAttribute("p") String profileId, Model model, Locale locale) {
//		model.addAttribute("robotPlayers", RobotPlayer.getRobotPlayers());
        return "/content/game/player/profile";
    }
}
