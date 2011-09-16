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
import wisematches.server.web.controllers.ServicePlayer;
import wisematches.server.web.controllers.WisematchesController;
import wisematches.server.web.i18n.GameMessageSource;
import wisematches.server.web.services.ads.AdvertisementManager;
import wisematches.server.web.services.search.player.PlayerInfoBean;
import wisematches.server.web.services.search.player.PlayerSearchArea;
import wisematches.server.web.services.search.player.PlayerSearchManager;
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
	private PlayerSearchManager searchManager;
	private AdvertisementManager advertisementManager;

	private static final Object[][] EMPTY_DATA = new Object[0][0];

	private static final String[] SORT_COLUMNS = {"account", "rating", "language", "activeGames", "finishedGames", "averageMoveTime"};
	private static final List<PlayerSearchArea> AREAS = Arrays.asList(PlayerSearchArea.FRIENDS, PlayerSearchArea.FORMERLY, PlayerSearchArea.PLAYERS);

	private static final Log log = LogFactory.getLog("wisematches.server.web.search");

	public PlayerSearchController() {
	}

	@RequestMapping("")
	public String showPlayersSearchForm(Model model, Locale locale) {
		model.addAttribute("scriplet", Boolean.FALSE);
		model.addAttribute("area", PlayerSearchArea.FRIENDS);
		model.addAttribute("areas", AREAS);
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
		final int gamesCount = searchManager.getPlayersCount(personality, area, null);
		final int displayGamesCount = searchManager.getPlayersCount(personality, area, null); // not null if we have criterias

		final Map<String, Object> res = new HashMap<String, Object>();
		res.put("sEcho", request.get("sEcho"));
		res.put("iTotalRecords", gamesCount);
		res.put("iTotalDisplayRecords", displayGamesCount);

		if (gamesCount == 0) {
			res.put("aaData", EMPTY_DATA);
		} else {
			final Order[] orders = new Order[(Integer) request.get("iSortingCols")];
			for (int i = 0; i < orders.length; i++) {
				final String name = SORT_COLUMNS[(Integer) request.get("iSortCol_" + i)];
				if ("asc".equalsIgnoreCase((String) request.get("sSortDir_" + i))) {
					orders[i] = Order.asc(name);
				} else {
					orders[i] = Order.desc(name);
				}
			}

			Range limit = Range.limit((Integer) request.get("iDisplayStart"), (Integer) request.get("iDisplayLength"));
			final List<PlayerInfoBean> games = searchManager.getPlayerBeans(personality, area, null, limit, orders);
			int index = 0;
			final Object[][] data = new Object[games.size()][];
			for (PlayerInfoBean info : games) {
				final Object[] row = new Object[SORT_COLUMNS.length + 1];
				row[0] = ServicePlayer.get(info.getAccount(), stateManager);
				row[1] = info.getRating();
				row[2] = info.getLastMoveTime() != null ? messageSource.formatDate(info.getLastMoveTime(), locale) : messageSource.getMessage("search.err.nomoves", locale);
				row[3] = messageSource.getMessage("language." + info.getAccount().getLanguage().code(), locale);
				row[4] = info.getActiveGames();
				row[5] = info.getFinishedGames();
				row[6] = messageSource.formatMinutes(info.getAverageMoveTime(), locale);
				data[index++] = row;
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
	public void setSearchManager(PlayerSearchManager searchManager) {
		this.searchManager = searchManager;
	}

	@Autowired
	public void setAdvertisementManager(AdvertisementManager advertisementManager) {
		this.advertisementManager = advertisementManager;
	}
}
