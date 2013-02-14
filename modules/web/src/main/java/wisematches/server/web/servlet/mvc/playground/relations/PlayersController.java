package wisematches.server.web.servlet.mvc.playground.relations;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.core.Personality;
import wisematches.server.services.relations.PlayerEntityBean;
import wisematches.server.services.relations.PlayerSearchArea;
import wisematches.server.services.relations.ScribblePlayerSearchManager;
import wisematches.server.services.state.PlayerStateManager;
import wisematches.server.web.servlet.mvc.ServicePlayer;
import wisematches.server.web.servlet.mvc.playground.AbstractSearchController;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/players")
public class PlayersController extends AbstractSearchController<PlayerEntityBean, PlayerSearchArea> {
	private PlayerStateManager stateManager;

	private static final List<PlayerSearchArea> AREAS = Arrays.asList(PlayerSearchArea.FRIENDS, PlayerSearchArea.FORMERLY, PlayerSearchArea.PLAYERS);

	private static final Log log = LogFactory.getLog("wisematches.server.web.search");

	public PlayersController() {
		super(new String[]{"nickname", "ratingG", "ratingA", "language", "activeGames", "finishedGames", "averageMoveTime", "lastMoveTime"});
	}

	@RequestMapping("")
	public String showPlayersSearchForm(@RequestParam(value = "area", required = false, defaultValue = "FRIENDS") PlayerSearchArea area, Model model) {
		model.addAttribute("searchArea", area);
		model.addAttribute("searchAreas", AREAS);
		model.addAttribute("searchColumns", getColumns());
		model.addAttribute("searchEntityDescriptor", getEntityDescriptor());
		return "/content/playground/players/search/view";
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
		map.put("nickname", ServicePlayer.get(info.getPid(), info.getNickname(), info.getPlayerType(), stateManager));
		if (info.getLanguage() != null) {
			map.put("language", messageSource.getMessage("language." + info.getLanguage().getCode(), locale));
		} else {
			map.put("language", messageSource.getMessage("search.err.language", locale));
		}
		map.put("ratingG", info.getRatingG());
		map.put("ratingA", info.getRatingA());
		map.put("lastMoveTime", info.getLastMoveTime() != null ? messageSource.formatDate(info.getLastMoveTime(), locale) : messageSource.getMessage("search.err.nomoves", locale));
		map.put("activeGames", info.getActiveGames());
		map.put("finishedGames", info.getFinishedGames());
//		map.put("playerOnline", stateManager.isPlayerOnline(Personality.person(info.getPid())));
		map.put("averageMoveTime", info.getAverageMoveTime() != 0 ? messageSource.formatTimeMinutes((long) (info.getAverageMoveTime() / 1000 / 60), locale) : messageSource.getMessage("search.err.nomoves", locale));
	}

	@Autowired
	public void setStateManager(PlayerStateManager stateManager) {
		this.stateManager = stateManager;
	}

	@Autowired
	public void setPlayerSearchManager(ScribblePlayerSearchManager playerSearchManager) {
		setEntitySearchManager(playerSearchManager);
	}
}
