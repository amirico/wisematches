package wisematches.server.web.servlet.mvc.playground.scribble.game;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wisematches.core.*;
import wisematches.playground.BoardCreationException;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.DictionaryManager;
import wisematches.playground.restriction.Restriction;
import wisematches.playground.restriction.RestrictionManager;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.server.services.relations.PlayerSearchArea;
import wisematches.server.services.relations.ScribblePlayerSearchManager;
import wisematches.server.web.servlet.mvc.playground.scribble.game.form.CreateScribbleForm;
import wisematches.server.web.servlet.mvc.playground.scribble.game.form.CreateScribbleTab;
import wisematches.server.web.servlet.sdo.ServiceResponse;

import javax.validation.Valid;
import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble")
@Deprecated
public class CreateGameController extends AbstractGameController {
	private DictionaryManager dictionaryManager;
	private RestrictionManager restrictionManager;
	private ScribblePlayerSearchManager playerSearchManager;

	private static final String[] SEARCH_COLUMNS = new String[]{"player", "ratingG", "ratingA", "language", "averageMoveTime", "lastMoveTime"};
	private static final List<PlayerSearchArea> SEARCH_AREAS = Arrays.asList(PlayerSearchArea.values());

	private static final Log log = LogFactory.getLog("wisematches.server.web.game.create");

	public CreateGameController() {
	}

	@RequestMapping("create")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String createGamePage(@RequestParam(value = "t", required = false) String type,
								 @RequestParam(value = "p", required = false) String parameter,
								 @Valid @ModelAttribute("create") CreateScribbleForm form,
								 Model model, Locale locale) {
		if (form.getBoardLanguage() == null) {
			form.setBoardLanguage(locale.getLanguage());
		}

		if (!form.isRotten()) {
			if ("robot".equalsIgnoreCase(type)) {
				initRobotForm(form, parameter);
			} else if ("wait".equalsIgnoreCase(type)) {
				initWaitOpponentForm(form, parameter);
			} else if ("challenge".equalsIgnoreCase(type)) {
				initChallengeForm(form, parameter, locale);
			} else if ("board".equalsIgnoreCase(type)) {
				initBoardCloneForm(form, parameter, locale);
			} else {
				initDefaultForm(form);
			}
		}

		final Player player = getPrincipal();
		model.addAttribute("robots", playManager.getSupportedRobots());
		model.addAttribute("restriction", restrictionManager.validateRestriction(player, "games.active", getActiveGamesCount(player)));
		model.addAttribute("maxOpponents", restrictionManager.getRestrictionThreshold("scribble.opponents", player));

		model.addAttribute("searchArea", PlayerSearchArea.FRIENDS);
		model.addAttribute("searchAreas", SEARCH_AREAS);
		model.addAttribute("searchColumns", SEARCH_COLUMNS);
		model.addAttribute("searchEntityDescriptor", playerSearchManager.getEntityDescriptor());

		if (player.getType().isVisitor()) {
			form.setCreateTab(CreateScribbleTab.ROBOT);
			model.addAttribute("playRobotsOnly", true);
		} else {
			model.addAttribute("playRobotsOnly", false);
		}
		return "/content/playground/scribble/create";
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "create.ajax", method = RequestMethod.POST)
	public ServiceResponse createGameService(@RequestBody CreateScribbleForm form, Locale locale) {
		final Player player = getPrincipal();
		if (form.getTitle().length() > 150) {
			return responseFactory.failure("game.create.title.err.max", locale);
		}
		if (form.getDaysPerMove() < 2) {
			return responseFactory.failure("game.create.time.err.min", locale);
		} else if (form.getDaysPerMove() > 14) {
			return responseFactory.failure("game.create.time.err.max", locale);
		}

		if (form.getCreateTab() == null) {
			return responseFactory.failure("game.create.opponent.err.blank", locale);
		}

		if (form.getChallengeMessage().length() > 254) {
			return responseFactory.failure("game.create.opponent.challenge.err", locale);
		}

		final Language language = Language.byCode(form.getBoardLanguage());
		if (language == null) {
			return responseFactory.failure("game.create.language.err.blank", locale);
		}

		final Dictionary dictionary = dictionaryManager.getDictionary(language);
		if (dictionary == null) {
			return responseFactory.failure("game.create.dictionary.err.unknown", locale);
		}

		final int activeGamesCount = getActiveGamesCount(player);
		if (restrictionManager.validateRestriction(player, "games.active", activeGamesCount) != null) {
			return responseFactory.failure("game.create.forbidden", new Object[]{activeGamesCount}, locale);
		}

		final ScribbleSettings s = new ScribbleSettings(form.getTitle(), language, form.getDaysPerMove());

		try {
			ScribbleBoard board = null;
			if (form.getCreateTab() == CreateScribbleTab.ROBOT) {
				board = playManager.createBoard(s, player, form.getRobotType());
			} else if (form.getCreateTab() == CreateScribbleTab.WAIT) {
				final int count = form.getOpponentsCount();
				final ServiceResponse response = validateOpponentsCount(player, count, locale);
				if (response != null) {
					return response;
				}
				proposalManager.initiate(s, player, count, form.createPlayerCriterion());
			} else if (form.getCreateTab() == CreateScribbleTab.CHALLENGE) {
				final long[] opponents = form.getOpponents();
				final ServiceResponse response = validateOpponentsCount(player, opponents.length, locale);
				if (response != null) {
					return response;
				}

				final Collection<Player> players = new ArrayList<>(opponents.length);
				for (long opponent : opponents) {
					Player p = personalityManager.getMember(opponent);
					if (p == null) {
						return responseFactory.failure("game.create.opponents.err.unknown", new Object[]{opponent}, locale);
					} else if (players.contains(p)) {
						return responseFactory.failure("game.create.opponents.err.duplicate", locale);
					} else {
						players.add(p);
					}
				}
				proposalManager.initiate(s, form.getChallengeMessage(), player, players);
			} else {
				return responseFactory.failure("game.create.opponent.err.incorrect", locale);
			}

			if (board == null) {
				return responseFactory.success();
			} else {
				return responseFactory.success(Collections.singletonMap("board", board.getBoardId()));
			}
		} catch (BoardCreationException ex) {
			log.error("New board can't be created: " + form, ex);
			return responseFactory.failure("game.create.opponents.err.unknown", locale);
		}
	}

	private void initRobotForm(CreateScribbleForm form, String parameter) {
		form.setCreateTab(CreateScribbleTab.ROBOT);

		RobotType type = RobotType.TRAINEE;
		if (parameter != null) {
			try {
				type = RobotType.valueOf(parameter.toUpperCase());
			} catch (IllegalArgumentException ignore) {
			}
		}
		form.setRobotType(type);
	}

	private void initWaitOpponentForm(CreateScribbleForm form, String parameter) {
		form.setCreateTab(CreateScribbleTab.WAIT);
		form.setOpponentsCount(1);
		if (parameter != null) {
			try {
				form.setOpponentsCount(Integer.valueOf(parameter));
			} catch (NumberFormatException ignore) {
			}
		}
	}

	private void initChallengeForm(CreateScribbleForm form, String parameter, Locale locale) {
		form.setTitle(messageSource.getMessage("game.challenge.player.label", getPrincipal(Member.class).getNickname(), locale));
		form.setChallengeMessage("");
		form.setCreateTab(CreateScribbleTab.CHALLENGE);
		final List<Long> ids = new ArrayList<>();
		if (parameter != null) {
			final String[] split = parameter.split("\\|");
			for (String id : split) {
				try {
					Personality player = personalityManager.getMember(Long.valueOf(id));
					if (player != null) {
						ids.add(player.getId());
					}
				} catch (NumberFormatException ignore) {
				}
			}

			if (ids.size() != 0) {
				final long[] res = new long[ids.size()];
				for (int i = 0, idsSize = ids.size(); i < idsSize; i++) {
					res[i] = ids.get(i);
				}
				form.setOpponents(res);
			}
		}
	}

	private void initBoardCloneForm(CreateScribbleForm form, String parameter, Locale locale) {
		try {
			final ScribbleBoard board = playManager.openBoard(Long.valueOf(parameter));
			if (board != null) {
				form.setTitle(messageSource.getMessage("game.challenge.replay.label", board.getBoardId(), locale));
				form.setChallengeMessage(messageSource.getMessage("game.challenge.replay.description", messageSource.getPersonalityNick(getPrincipal(), locale), locale));
				form.setDaysPerMove(board.getSettings().getDaysPerMove());
				form.setBoardLanguage(board.getSettings().getLanguage().getCode());

				int index = 0;
				final List<Personality> playersHands = board.getPlayers();
				final long[] players = new long[playersHands.size() - 1];
				for (Personality playersHand : playersHands) {
					if (playersHand.equals(getPrincipal())) {
						continue;
					}
					players[index++] = playersHand.getId();
				}
				form.setOpponents(players);
				form.setCreateTab(CreateScribbleTab.CHALLENGE);
			}
		} catch (BoardLoadingException ignore) { // do nothing
		}
	}

	private void initDefaultForm(CreateScribbleForm form) {
		form.setCreateTab(CreateScribbleTab.ROBOT);
		form.setOpponentsCount(1);
	}

	private ServiceResponse validateOpponentsCount(Player player, int count, Locale locale) {
		if (count < 1) {
			return responseFactory.failure("game.create.opponents.err.min", locale);
		} else if (count > 3) {
			return responseFactory.failure("game.create.opponents.err.max", locale);
		}
		final Restriction restriction = restrictionManager.validateRestriction(player, "scribble.opponents", count - 1);
		if (restriction != null) {
			return responseFactory.failure("game.create.opponents.err.count", new Object[]{restriction.getThreshold()}, locale);
		}
		return null;
	}

	@Autowired
	public void setDictionaryManager(DictionaryManager dictionaryManager) {
		this.dictionaryManager = dictionaryManager;
	}

	@Autowired
	public void setRestrictionManager(RestrictionManager restrictionManager) {
		this.restrictionManager = restrictionManager;
	}

	@Autowired
	public void setPlayerSearchManager(ScribblePlayerSearchManager playerSearchManager) {
		this.playerSearchManager = playerSearchManager;
	}
}
