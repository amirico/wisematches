package wisematches.server.web.controllers.gameplaying;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.server.personality.player.Player;
import wisematches.server.personality.player.PlayerManager;
import wisematches.server.web.controllers.AbstractPlayerController;
import wisematches.server.web.controllers.UnknownEntityException;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/game")
public class PlayerProfileController extends AbstractPlayerController {
	private PlayerManager playerManager;

	public PlayerProfileController() {
	}

	@RequestMapping("profile")
	public String createGamePage(@RequestParam("p") String profileId, Model model, Locale locale) throws UnknownEntityException {
		try {
			final Player player = playerManager.getPlayer(Long.parseLong(profileId));
			if (player == null) {
				throw new UnknownEntityException(profileId, "profile");
			}
			model.addAttribute("profile", player);
			return "/content/game/player/profile";
		} catch (NumberFormatException ex) {
			throw new UnknownEntityException(profileId, "profile");
		}
	}

	@Autowired
	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}
}
