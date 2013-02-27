package wisematches.server.web.servlet.mvc.playground.relations;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.core.Player;
import wisematches.server.services.relations.PlayerEntityBean;
import wisematches.server.services.relations.PlayerSearchArea;
import wisematches.server.services.relations.ScribblePlayerSearchManager;
import wisematches.server.web.servlet.mvc.playground.AbstractSearchController;
import wisematches.server.web.servlet.sdo.PersonalityData;
import wisematches.server.web.servlet.sdo.ServiceResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/players")
@Deprecated
public class PlayersController extends AbstractSearchController<PlayerEntityBean, PlayerSearchArea> {
	private static final List<PlayerSearchArea> AREAS = Arrays.asList(PlayerSearchArea.FRIENDS, PlayerSearchArea.FORMERLY, PlayerSearchArea.PLAYERS);

	private static final Logger log = LoggerFactory.getLogger("wisematches.web.mvc.PlayersController");

	public PlayersController() {
		super(new String[]{"player", "ratingG", "ratingA", "activeGames", "finishedGames", "averageMoveTime", "lastMoveTime"});
	}

	@RequestMapping("")
	public String showPlayersPage(@RequestParam(value = "area", required = false, defaultValue = "FRIENDS") PlayerSearchArea area, Model model) {
		model.addAttribute("searchArea", area);
		model.addAttribute("searchAreas", AREAS);
		model.addAttribute("searchColumns", getColumns());
		model.addAttribute("searchEntityDescriptor", getEntityDescriptor());
		return "/content/playground/players/search/view";
	}

	@RequestMapping("load.ajax")
	public ServiceResponse loadPlayersService(@RequestParam("area") String areaName, @RequestBody Map<String, Object> request, Locale locale) {
		final Player player = getPrincipal();
		final PlayerSearchArea area = PlayerSearchArea.valueOf(areaName.toUpperCase());
		log.debug("Loading players for area: {} for player ", area, player);
		return responseFactory.success(loadData(player, request, area, locale));
	}

	@Override
	protected void convertEntity(PlayerEntityBean info, Map<String, Object> map, Locale locale) {
		map.put("player", PersonalityData.get(personalityManager.getPerson(info.getPlayer()), playerStateManager));
		if (info.getLanguage() != null) {
			map.put("language", messageSource.getMessage("language." + info.getLanguage().getCode(), locale));
		} else {
			map.put("language", messageSource.getMessage("search.err.language", locale));
		}
		map.put("ratingG", info.getRatingG());
		map.put("ratingA", info.getRatingA());
		map.put("activeGames", info.getActiveGames());
		map.put("finishedGames", info.getFinishedGames());
		map.put("lastMoveTime", info.getLastMoveTime() != null ? messageSource.formatDate(info.getLastMoveTime(), locale) : messageSource.getMessage("search.err.nomoves", locale));
		map.put("averageMoveTime", info.getAverageMoveTime() != 0 ? messageSource.formatTimeMinutes((long) (info.getAverageMoveTime() / 1000 / 60), locale) : messageSource.getMessage("search.err.nomoves", locale));
	}

	@Autowired
	public void setPlayerSearchManager(ScribblePlayerSearchManager playerSearchManager) {
		setEntitySearchManager(playerSearchManager);
	}
}
