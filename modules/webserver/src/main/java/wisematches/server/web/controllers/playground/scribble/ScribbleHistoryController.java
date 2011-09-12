package wisematches.server.web.controllers.playground.scribble;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wisematches.database.Order;
import wisematches.database.Range;
import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.personality.player.PlayerManager;
import wisematches.playground.history.GameHistoryManager;
import wisematches.playground.scribble.history.ScribbleGameHistory;
import wisematches.server.web.controllers.ServicePlayer;
import wisematches.server.web.controllers.UnknownEntityException;
import wisematches.server.web.controllers.WisematchesController;
import wisematches.server.web.i18n.GameMessageSource;
import wisematches.server.web.services.ads.AdvertisementManager;
import wisematches.server.web.services.state.PlayerStateManager;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble")
public class ScribbleHistoryController extends WisematchesController {
	private PlayerManager playerManager;
	private GameMessageSource messageSource;
	private PlayerStateManager playerStateManager;
	private AdvertisementManager advertisementManager;
	private GameHistoryManager<ScribbleGameHistory> historyManager;

	private static final String[] COLUMNS = {"finishedDate", "newRating", "ratingChange", "players", "resolution", "movesCount"};
	private static final Object[][] EMPTY_DATA = new Object[0][0];

	private static final Log log = LogFactory.getLog("wisematches.server.web.dashboard");

	public ScribbleHistoryController() {
	}

	@RequestMapping("history")
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
		model.addAttribute("columns", COLUMNS);
		if (principal.getMembership().isAdsVisible()) {
			model.addAttribute("advertisementBlock", advertisementManager.getAdvertisementBlock("dashboard", Language.byLocale(locale)));
		}
		return "/content/playground/scribble/history";
	}

	@ResponseBody
	@RequestMapping(value = "history", method = RequestMethod.POST)
	public Map<String, Object> loadHistoryGames(@RequestParam(value = "p", required = false) Long pid, @RequestBody Map<String, Object> request, Locale locale) {
		final Personality principal;
		if (pid == null) {
			principal = getPersonality();
		} else {
			principal = Personality.person(pid);
		}

		final int gamesCount = historyManager.getFinishedGamesCount(principal);

		final Map<String, Object> res = new HashMap<String, Object>();
		res.put("sEcho", request.get("sEcho"));
		res.put("iTotalRecords", gamesCount);
		res.put("iTotalDisplayRecords", gamesCount);

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
			final List<ScribbleGameHistory> games = historyManager.getFinishedGames(principal, limit, orders);
			int index = 0;
			final Object[][] data = new Object[games.size()][];
			for (ScribbleGameHistory game : games) {
				final Object[] row = new Object[COLUMNS.length + 1];
				row[0] = messageSource.formatDate(game.getFinishedDate(), locale);
				row[1] = game.getNewRating();
				row[2] = game.getRatingChange();

				final long[] playerIds = game.getPlayers(principal.getId());
				final ServicePlayer[] players = new ServicePlayer[playerIds.length];
				for (int i = 0, players1Length = playerIds.length; i < players1Length; i++) {
					players[i] = ServicePlayer.get(playerManager.getPlayer(playerIds[i]), messageSource, playerStateManager, locale);
				}
				row[3] = players;
				row[4] = messageSource.getMessage("game.resolution." + game.getResolution().name().toLowerCase(), locale);
				row[5] = game.getMovesCount();
				row[6] = game.getBoardId();
				data[index++] = row;
			}
			res.put("aaData", data);
		}
		return res;
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
	public void setHistoryManager(GameHistoryManager<ScribbleGameHistory> historyManager) {
		this.historyManager = historyManager;
	}
}
