package wisematches.server.web.servlet.mvc.playground.scribble.game;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.server.web.servlet.mvc.UnknownEntityException;

import java.util.Locale;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble/history")
public class HistoryGameController extends AbstractGameController { //extends AbstractSearchController<ScribbleHistoryEntity, GameResolution, SearchFilter> {
	private static final Log log = LogFactory.getLog("wisematches.server.web.dashboard");

	public HistoryGameController() {
//		super(new String[]{"title", "startedDate", "finishedDate", "players", "movesCount", "resolution", "newRating", "ratingChange"});
	}

	@RequestMapping("")
	public String showHistoryGames(@RequestParam(value = "p", required = false) Long pid, Model model) throws UnknownEntityException {
/*
		final Personality principal;
		if (pid == null) {
			principal = getPlayer();
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
		model.addAttribute("searchEntityDescriptor", getEntityDescriptor());
*/
		return "/content/playground/scribble/history";
	}

	@ResponseBody
	@RequestMapping("load.ajax")
	public Map<String, Object> loadHistoryGames(@RequestParam(value = "p", required = false) Long pid, @RequestBody Map<String, Object> request, Locale locale) {
		throw new UnsupportedOperationException("commented");
/*
		final Personality personality;
		if (pid == null) {
			personality = getPersonality();
		} else {
			personality = Personality.person(pid);
		}
		return loadData(personality, request, null, locale);
*/
	}

/*
	@Override
	protected void convertEntity(ScribbleHistoryEntity entity, Personality personality, Map<String, Object> map, Locale locale) {
		final GameRelationship relationship = entity.getRelationship();

		map.put("title", gameMessageSource.getBoardTitle(entity.getTitle(), relationship, locale));
		map.put("boardId", entity.getBoardId());
		map.put("relationship", relationship);
		map.put("newRating", entity.getNewRating());
		map.put("startedDate", messageSource.formatDate(entity.getStartedDate(), locale));
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
	}
*/

/*

	@Autowired
	public void setHistorySearchManager(ScribbleHistorySearchManager historySearchManager) {
		setEntitySearchManager(historySearchManager);
	}
*/
}
