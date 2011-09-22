package wisematches.server.web.controllers.playground.player;

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
import wisematches.playground.scribble.search.player.ScribblePlayerSearchManager;
import wisematches.playground.search.player.PlayerEntityBean;
import wisematches.playground.search.player.PlayerSearchArea;
import wisematches.server.web.controllers.ServicePlayer;
import wisematches.server.web.controllers.playground.AbstractSearchController;
import wisematches.server.web.i18n.GameMessageSource;
import wisematches.server.web.services.ads.AdvertisementManager;
import wisematches.server.web.services.state.PlayerStateManager;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/players")
public class PlayerSearchController extends AbstractSearchController<PlayerEntityBean, PlayerSearchArea> {
	private GameMessageSource messageSource;
	private PlayerStateManager stateManager;
	private AdvertisementManager advertisementManager;

	private static final List<PlayerSearchArea> AREAS = Arrays.asList(PlayerSearchArea.FRIENDS, PlayerSearchArea.FORMERLY, PlayerSearchArea.PLAYERS);

	private static final Log log = LogFactory.getLog("wisematches.server.web.search");

	public PlayerSearchController() {
		super(new String[]{"nickname", "language", "rating", "averageMoveTime", "lastMoveTime"});
	}

	@RequestMapping("")
	public String showPlayersSearchForm(@RequestParam(value = "area", required = false, defaultValue = "FRIENDS") PlayerSearchArea area, Model model, Locale locale) {
		model.addAttribute("searchArea", area);
		model.addAttribute("searchAreas", AREAS);
		model.addAttribute("searchColumns", getColumns());
		model.addAttribute("searchEntityDescriptor", getDesiredEntityDescriptor());

		if (getPrincipal().getMembership().isAdsVisible()) {
			model.addAttribute("advertisementBlock", advertisementManager.getAdvertisementBlock("players", Language.byLocale(locale)));
		}
		return "/content/playground/players/view";
	}

	@ResponseBody
	@RequestMapping("load.ajax")
	public Map<String, Object> load(@RequestParam("area") String areaName, @RequestBody Map<String, Object> request, Locale locale) {
		final Personality personality = getPersonality();
		final PlayerSearchArea area = PlayerSearchArea.valueOf(areaName.toUpperCase());
		if (log.isDebugEnabled()) {
			log.debug("Loading players for area: " + area + " for player " + personality);
		}
		return loadData(personality, request, area, locale);
	}

	@Override
	protected void convertEntity(PlayerEntityBean info, Personality personality, Map<String, Object> map, Locale locale) {
		map.put("nickname", ServicePlayer.get(info.getPid(), info.getNickname(), stateManager));
		if (info.getLanguage() != null) {
			map.put("language", messageSource.getMessage("language." + info.getLanguage().code(), locale));
		} else {
			map.put("language", messageSource.getMessage("search.err.language", locale));
		}
		map.put("rating", info.getRating());
		map.put("lastMoveTime", info.getLastMoveTime() != null ? messageSource.formatDate(info.getLastMoveTime(), locale) : messageSource.getMessage("search.err.nomoves", locale));
		map.put("activeGames", info.getActiveGames());
		map.put("finishedGames", info.getFinishedGames());
		map.put("averageMoveTime", info.getAverageMoveTime() != 0 ? messageSource.formatMinutes(info.getAverageMoveTime() / 1000 / 60, locale) : messageSource.getMessage("search.err.nomoves", locale));
	}

	@Autowired
	public void setStateManager(PlayerStateManager stateManager) {
		this.stateManager = stateManager;
	}

	@Autowired
	public void setMessageSource(GameMessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Autowired
	public void setPlayerSearchManager(ScribblePlayerSearchManager playerSearchManager) {
		setEntitySearchManager(playerSearchManager);
	}

	@Autowired
	public void setAdvertisementManager(AdvertisementManager advertisementManager) {
		this.advertisementManager = advertisementManager;
	}
}
