package wisematches.server.web.controllers.playground.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.personality.player.PlayerManager;
import wisematches.playground.blacklist.BlacklistManager;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.WisematchesController;
import wisematches.server.web.controllers.playground.player.form.BlacklistRecordForm;
import wisematches.server.web.i18n.GameMessageSource;
import wisematches.server.web.services.ads.AdvertisementManager;

import java.util.List;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/blacklist")
public class BlacklistController extends WisematchesController {
	private PlayerManager playerManager;
	private BlacklistManager blacklistManager;
	private GameMessageSource gameMessageSource;
	private AdvertisementManager advertisementManager;

	public BlacklistController() {
	}

	@RequestMapping("view")
	public String viewBlacklist(Model model, Locale locale) {
		final Player principal = getPrincipal();
		model.addAttribute("blacklist", blacklistManager.getBlacklist(principal));
		if (principal.getMembership().isAdsVisible()) {
			model.addAttribute("advertisementBlock", advertisementManager.getAdvertisementBlock("blacklist", Language.byLocale(locale)));
		}
		return "/content/playground/blacklist/view";
	}

	@ResponseBody
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public ServiceResponse addToBlacklist(@RequestBody BlacklistRecordForm form, Locale locale) {
		final Player player = playerManager.getPlayer(form.getPerson());
		if (player == null) {
			return ServiceResponse.failure(gameMessageSource.getMessage("blacklist.err.unknown", locale));
		}
		blacklistManager.addPlayer(getPersonality(), player, form.getComment());
		return ServiceResponse.SUCCESS;
	}

	@ResponseBody
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "remove", method = RequestMethod.POST)
	public ServiceResponse removeFromBlacklist(@RequestParam(value = "persons[]") List<Long> removeList) {
		final Personality personality = getPersonality();
		for (Long id : removeList) {
			blacklistManager.removePlayer(personality, Personality.person(id));
		}
		return ServiceResponse.SUCCESS;
	}

	@Autowired
	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	@Autowired
	public void setBlacklistManager(BlacklistManager blacklistManager) {
		this.blacklistManager = blacklistManager;
	}

	@Autowired
	public void setGameMessageSource(GameMessageSource gameMessageSource) {
		this.gameMessageSource = gameMessageSource;
	}

	@Autowired
	public void setAdvertisementManager(AdvertisementManager advertisementManager) {
		this.advertisementManager = advertisementManager;
	}
}
