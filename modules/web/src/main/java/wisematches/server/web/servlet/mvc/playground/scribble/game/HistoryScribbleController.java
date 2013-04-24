package wisematches.server.web.servlet.mvc.playground.scribble.game;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.core.Player;
import wisematches.core.search.Orders;
import wisematches.core.search.Range;
import wisematches.playground.scribble.ScribbleDescription;
import wisematches.server.web.servlet.mvc.UnknownEntityException;
import wisematches.server.web.servlet.mvc.playground.scribble.AbstractScribbleController;
import wisematches.server.web.servlet.sdo.DataTablesRequest;
import wisematches.server.web.servlet.sdo.DataTablesResponse;
import wisematches.server.web.servlet.sdo.ServiceResponse;
import wisematches.server.web.servlet.sdo.scribble.game.GameInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble/history")
public class HistoryScribbleController extends AbstractScribbleController {
	private static final List<Object> NO_ROWS_LIST = Collections.emptyList();
	private static final Logger log = LoggerFactory.getLogger("wisematches.web.mvc.HistoryGameController");

	public HistoryScribbleController() {
	}

	@RequestMapping("")
	public String showHistoryGames(@RequestParam(value = "p", required = false) Long pid, Model model) throws UnknownEntityException {
		final Player player = getPlayerToShow(pid);
		if (player == null) {
			throw new UnknownEntityException(null, "account");
		}

		log.debug("Loading active games for player: {}", player);
		model.addAttribute("player", player);
		return "/content/playground/scribble/history";
	}

	@RequestMapping("load.ajax")
	public ServiceResponse loadHistoryGames(@RequestParam(value = "p", required = false) Long pid, @RequestBody DataTablesRequest tableRequest, Locale locale) {
		final Player player = getPlayerToShow(pid);
		if (player == null) {
			return responseFactory.success(new DataTablesResponse(0, Collections.emptyList(), tableRequest));
		}

		final int totalCount = searchManager.getTotalCount(player, FINISHED_GAMES_CTX);
		if (totalCount == 0) {
			return responseFactory.success(new DataTablesResponse(0, NO_ROWS_LIST, tableRequest));
		} else {
			Orders orders = tableRequest.getOrders();
			if (orders != null) {
				orders = orders.map(Collections.singletonMap("scores", "newRating-oldRating"));
			}
			final Range limit = tableRequest.getLimit();
			final List<ScribbleDescription> descriptors = searchManager.searchEntities(player, FINISHED_GAMES_CTX, orders, limit);
			final List<GameInfo> rows = new ArrayList<>(descriptors.size());
			for (ScribbleDescription description : descriptors) {
				rows.add(new GameInfo(description, messageSource, playerStateManager, locale));
			}
			return responseFactory.success(new DataTablesResponse(totalCount, rows, tableRequest));
		}
	}

	private Player getPlayerToShow(Long pid) {
		final Player player;
		if (pid == null) {
			player = getPrincipal();
		} else {
			player = personalityManager.getMember(pid);
		}
		return player;
	}
}
