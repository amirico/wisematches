package wisematches.server.web.controllers.playground.scribble;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.personality.player.PlayerManager;
import wisematches.playground.scribble.search.board.ScribbleHistoryEntity;
import wisematches.playground.scribble.search.board.ScribbleHistorySearchManager;
import wisematches.server.web.controllers.ServicePlayer;
import wisematches.server.web.controllers.UnknownEntityException;
import wisematches.server.web.controllers.playground.AbstractSearchController;
import wisematches.server.web.i18n.GameMessageSource;
import wisematches.server.web.services.ads.AdvertisementManager;
import wisematches.server.web.services.state.PlayerStateManager;

import java.util.Locale;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble/history")
public class ScribbleHistoryController extends AbstractSearchController<ScribbleHistoryEntity, Void> {
	private PlayerManager playerManager;
	private GameMessageSource messageSource;
	private PlayerStateManager playerStateManager;
	private AdvertisementManager advertisementManager;

	private static final Log log = LogFactory.getLog("wisematches.server.web.dashboard");

	public ScribbleHistoryController() {
		super(new String[]{"finishedDate", "newRating", "ratingChange", "players", "resolution", "movesCount"});
	}

	@RequestMapping("")
	public String showHistoryGames(@RequestParam(value = "p", required = false) Long pid, Model model, Locale locale) throws UnknownEntityException {
		final Player principal;
		if (pid == null) {
			principal = getPrincipal();
		} else {
			principal = playerManager.getPlayer(pid);
		}
		if (principal == null) {
			throw new UnknownEntityException(null, "account");
		}

		if (log.isDebugEnabled()) {
			log.debug("Loading active games for personality: " + principal);
		}
		model.addAttribute("player", principal);
		model.addAttribute("searchColumns", getColumns());
		model.addAttribute("searchEntityDescriptor", getDesiredEntityDescriptor());

		if (principal.getMembership().isAdsVisible()) {
			model.addAttribute("advertisementBlock", advertisementManager.getAdvertisementBlock("dashboard", Language.byLocale(locale)));
		}
		return "/content/playground/scribble/history";
	}

	@ResponseBody
	@RequestMapping(value = "load.ajax")
	public Map<String, Object> loadHistoryGames(@RequestParam(value = "p", required = false) Long pid, @RequestBody Map<String, Object> request, Locale locale) {
		final Personality personality;
		if (pid == null) {
			personality = getPersonality();
		} else {
			personality = Personality.person(pid);
		}
		return loadData(personality, request, null, locale);
	}

	@Override
	protected void convertEntity(ScribbleHistoryEntity entity, Personality personality, Map<String, Object> map, Locale locale) {
		map.put("newRating", entity.getNewRating());
		map.put("finishedDate", messageSource.formatDate(entity.getFinishedDate(), locale));
		map.put("ratingChange", entity.getRatingChange());
		final long[] playerIds = entity.getPlayers(personality);
		final ServicePlayer[] players = new ServicePlayer[playerIds.length];
		for (int i = 0, players1Length = playerIds.length; i < players1Length; i++) {
			players[i] = ServicePlayer.get(playerManager.getPlayer(playerIds[i]), messageSource, playerStateManager, locale);
		}
		map.put("players", players);
		map.put("resolution", messageSource.getMessage("game.resolution." + entity.getResolution().name().toLowerCase(), locale));
		map.put("movesCount", entity.getMovesCount());
		map.put("boardId", entity.getBoardId());
	}

	@Autowired
	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	@Autowired
	public void setMessageSource(GameMessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Autowired
	public void setPlayerStateManager(PlayerStateManager playerStateManager) {
		this.playerStateManager = playerStateManager;
	}

	@Autowired
	public void setAdvertisementManager(AdvertisementManager advertisementManager) {
		this.advertisementManager = advertisementManager;
	}

	@Autowired
	public void setHistorySearchManager(ScribbleHistorySearchManager historySearchManager) {
		setEntitySearchManager(historySearchManager);
	}
}
