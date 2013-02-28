package wisematches.server.web.servlet.mvc.playground.scribble.game;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.core.Personality;
import wisematches.core.Player;
import wisematches.playground.GameRelationship;
import wisematches.playground.scribble.ScribbleDescription;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.server.web.servlet.mvc.UnknownEntityException;
import wisematches.server.web.servlet.mvc.playground.scribble.game.form.HistoryGameForm;
import wisematches.server.web.servlet.sdo.ServiceResponse;
import wisematches.server.web.servlet.sdo.person.PersonalityData;
import wisematches.server.web.servlet.sdo.table.DataTableRequest;
import wisematches.server.web.servlet.sdo.table.DataTableResponse;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@Deprecated
@RequestMapping("/playground/scribble/history")
public class HistoryGameController extends AbstractGameController {
	private static final List<Object> NO_ROWS_LIST = Collections.emptyList();
	private static final Logger log = LoggerFactory.getLogger("wisematches.web.mvc.HistoryGameController");

	public HistoryGameController() {
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
	public ServiceResponse loadHistoryGames(@RequestParam(value = "p", required = false) Long pid, @RequestBody Map<String, Object> request, Locale locale) {
		final Player player = getPlayerToShow(pid);
		if (player == null) {
			return responseFactory.failure("game.history.err.player", locale);
		}

		final DataTableRequest tableRequest = new DataTableRequest(request);
		final int totalCount = searchManager.getTotalCount(player, FINISHED_GAMES_CTX);
		if (totalCount == 0) {
			return responseFactory.success(new DataTableResponse(0, NO_ROWS_LIST, tableRequest));
		} else {
			final List<ScribbleDescription> descriptors = searchManager.searchEntities(player, FINISHED_GAMES_CTX, tableRequest.getOrders(), tableRequest.getLimit());
			final List<HistoryGameForm> rows = new ArrayList<>(descriptors.size());
			for (ScribbleDescription description : descriptors) {
				rows.add(convertDescriptor(description, player, locale));
			}
			return responseFactory.success(new DataTableResponse(totalCount, rows, tableRequest));
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

	private HistoryGameForm convertDescriptor(ScribbleDescription description, Player player, Locale locale) {
		final ScribbleSettings settings = description.getSettings();
		final GameRelationship relationship = description.getRelationship();

		final HistoryGameForm entry = new HistoryGameForm();
		entry.setBoardId(description.getBoardId());
		entry.setTitle(messageSource.getBoardTitle(settings.getTitle(), relationship, locale));
		entry.setLanguage(messageSource.getMessage("language." + settings.getLanguage().name().toLowerCase(), locale));
		entry.setResolution(messageSource.getMessage("game.resolution." + description.getResolution().name().toLowerCase(), locale));
		entry.setMovesCount(description.getMovesCount());
		entry.setRatingChange(description.getPlayerHand(player));
		entry.setStartedDate(messageSource.formatDate(description.getStartedDate(), locale));
		entry.setFinishedDate(messageSource.formatDate(description.getFinishedDate(), locale));

		final List<Personality> players1 = description.getPlayers();
		final PersonalityData[] ps = new PersonalityData[players1.size() - 1];
		int index = 0;
		for (Personality person : players1) {
			if (!person.equals(player)) {
				ps[index++] = PersonalityData.get(messageSource.getPersonalityNick(person, locale), person, playerStateManager);
			}
		}
		entry.setOpponents(ps);
		return entry;
	}
}
