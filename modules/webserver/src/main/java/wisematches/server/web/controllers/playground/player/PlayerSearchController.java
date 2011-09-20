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
import wisematches.database.Order;
import wisematches.database.Range;
import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.playground.search.DesiredEntityDescriptor;
import wisematches.playground.search.EntitySearchManager;
import wisematches.playground.search.player.PlayerEntityBean;
import wisematches.playground.search.player.PlayerSearchArea;
import wisematches.server.web.controllers.ServicePlayer;
import wisematches.server.web.controllers.WisematchesController;
import wisematches.server.web.i18n.GameMessageSource;
import wisematches.server.web.services.ads.AdvertisementManager;
import wisematches.server.web.services.state.PlayerStateManager;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/players")
public class PlayerSearchController extends WisematchesController {
	private GameMessageSource messageSource;
	private PlayerStateManager stateManager;
	private AdvertisementManager advertisementManager;
	private EntitySearchManager<PlayerEntityBean, PlayerSearchArea> searchManager;

	private static final Object[][] EMPTY_DATA = new Object[0][0];

	private static final String[] COLUMNS = {"nickname", "language", "rating", "averageMoveTime", "lastMoveTime"};
	private static final List<PlayerSearchArea> AREAS = Arrays.asList(PlayerSearchArea.FRIENDS, PlayerSearchArea.FORMERLY, PlayerSearchArea.PLAYERS);

	private static final Log log = LogFactory.getLog("wisematches.server.web.search");

	public PlayerSearchController() {
	}

	@RequestMapping("")
	public String showPlayersSearchForm(@RequestParam(value = "area", required = false, defaultValue = "FRIENDS") PlayerSearchArea area, Model model, Locale locale) {
		model.addAttribute("searchArea", area);
		model.addAttribute("searchAreas", AREAS);
		model.addAttribute("searchColumns", COLUMNS);
		model.addAttribute("searchEntityDescriptor", searchManager.getDescriptor());

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
		final int gamesCount = searchManager.getTotalCount(personality, area);
		final int displayGamesCount = gamesCount;

		final Map<String, Object> res = new HashMap<String, Object>();
		res.put("sEcho", request.get("sEcho"));
		res.put("iTotalRecords", gamesCount);
		res.put("iTotalDisplayRecords", displayGamesCount);

		if (gamesCount == 0) {
			res.put("aaData", EMPTY_DATA);
		} else {
			final Order[] orders = new Order[(Integer) request.get("iSortingCols")];
			for (int i = 0; i < orders.length; i++) {
				final String name = COLUMNS[(Integer) request.get("iSortCol_" + i)];
				if ("asc".equalsIgnoreCase((String) request.get("sSortDir_" + i))) {
					orders[i] = Order.asc(name);
				} else {
					orders[i] = Order.desc(name);
				}
			}

			Range limit = Range.limit((Integer) request.get("iDisplayStart"), (Integer) request.get("iDisplayLength"));
			final List<PlayerEntityBean> games = searchManager.searchEntities(personality, area, null, orders, limit);
			int index = 0;
			final Object[] data = new Object[games.size()];

			final String noMovesMsg = messageSource.getMessage("search.err.nomoves", locale);
			for (PlayerEntityBean info : games) {
				final Map<String, Object> a = new HashMap<String, Object>();
				a.put("nickname", ServicePlayer.get(info.getPid(), info.getNickname(), stateManager));

				if (info.getLanguage() != null) {
					a.put("language", messageSource.getMessage("language." + info.getLanguage().code(), locale));
				} else {
					a.put("language", messageSource.getMessage("search.err.language", locale));
				}
				a.put("rating", info.getRating());
				a.put("lastMoveTime", info.getLastMoveTime() != null ? messageSource.formatDate(info.getLastMoveTime(), locale) : noMovesMsg);
				a.put("activeGames", info.getActiveGames());
				a.put("finishedGames", info.getFinishedGames());
				a.put("averageMoveTime", info.getAverageMoveTime() != 0 ? messageSource.formatMinutes(info.getAverageMoveTime() / 1000 / 60, locale) : noMovesMsg);
				data[index++] = a;
			}
			res.put("aaData", data);
		}
		return res;
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
	public void setSearchManager(EntitySearchManager<PlayerEntityBean, PlayerSearchArea> searchManager) {
		this.searchManager = searchManager;
	}

	@Autowired
	public void setAdvertisementManager(AdvertisementManager advertisementManager) {
		this.advertisementManager = advertisementManager;
	}
}
