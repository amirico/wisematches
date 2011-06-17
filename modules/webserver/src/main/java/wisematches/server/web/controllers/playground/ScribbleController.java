package wisematches.server.web.controllers.playground;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import wisematches.personality.Language;
import wisematches.personality.Membership;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.personality.player.PlayerManager;
import wisematches.personality.player.computer.ComputerPlayer;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.playground.BoardManagementException;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.propose.GameProposalManager;
import wisematches.playground.propose.ViolatedRestrictionException;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleBoardManager;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.playground.scribble.search.ScribbleSearchesEngine;
import wisematches.server.web.controllers.AbstractPlayerController;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.playground.form.CreateScribbleForm;
import wisematches.server.web.services.ads.AdvertisementManager;
import wisematches.server.web.services.restriction.PlayerRestrictionManager;
import wisematches.server.web.services.restriction.RestrictionException;

import javax.validation.Valid;
import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble")
public class ScribbleController extends AbstractPlayerController {
	private PlayerManager playerManager;
	private ScribbleBoardManager boardManager;
	private ScribbleSearchesEngine searchesEngine;
	private AdvertisementManager advertisementManager;
	private PlayerRestrictionManager restrictionManager;
	private GameProposalManager<ScribbleSettings> proposalManager;

	private static final Log log = LogFactory.getLog("wisematches.server.web.dashboard");

	public ScribbleController() {
	}

	@RequestMapping("create")
	public String createGamePage(@ModelAttribute("create") CreateScribbleForm form, Model model, Locale locale) {
		if (form.getBoardLanguage() == null) {
			form.setBoardLanguage(locale.getLanguage());
		}

		final Player principal = getPrincipal();

		if (principal.getMembership() == Membership.GUEST && form.getOpponent1() == null) {
			form.setOpponent1(String.valueOf(RobotPlayer.DULL.getId()));
		}

		model.addAttribute("robotPlayers", RobotPlayer.getRobotPlayers());
		model.addAttribute("opponentsCount", restrictionManager.getRestriction(principal, "scribble.opponents"));
		model.addAttribute("gamesCount", restrictionManager.getRestriction(principal, "games.active"));
		model.addAttribute("restricted", restrictionManager.isRestricted(principal, "games.active", getActiveGamesCount()));
		model.addAttribute("playRobotsOnly", principal.getMembership() == Membership.GUEST);
		if (principal.getMembership().isAdsVisible()) {
			model.addAttribute("advertisementBlock", advertisementManager.getAdvertisementBlock("dashboard", Language.byLocale(locale)));
		}
		return "/content/playground/scribble/create";
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String createGameAction(@Valid @ModelAttribute("create") CreateScribbleForm form,
								   BindingResult result, Model model, Locale locale) throws BoardManagementException, RestrictionException {
		if (log.isInfoEnabled()) {
			log.info("Create new game: " + form);
		}

		final Player principal = getPrincipal();
		restrictionManager.checkRestriction(principal, "games.active", getActiveGamesCount());

		if (form.getDaysPerMove() < 2) {
			result.rejectValue("daysPerMove", "game.create.time.err.min");
		} else if (form.getDaysPerMove() > 14) {
			result.rejectValue("daysPerMove", "game.create.time.err.max");
		}

		int opponents = 0;
		final List<Personality> players = new ArrayList<Personality>();
		if (checkOpponent("opponent1", form.getOpponent1(), true, players, result)) {
			opponents++;
		}
		if (checkOpponent("opponent2", form.getOpponent2(), false, players, result)) {
			opponents++;
		}
		if (checkOpponent("opponent3", form.getOpponent3(), false, players, result)) {
			opponents++;
		}

		if (opponents == 0) {
			result.rejectValue("opponent1", "game.create.opponents.err.nofirst");
		}


		if (!result.hasErrors()) {
			final ScribbleSettings s = new ScribbleSettings(form.getTitle(), form.getBoardLanguage(), form.getDaysPerMove());
			if (players.size() == opponents) {
				players.add(0, principal); // also add current personality as a first one
				final ScribbleBoard board = boardManager.createBoard(s, players);
				return "redirect:/playground/scribble/board?b=" + board.getBoardId();
			} else {
				proposalManager.initiateGameProposal(s, opponents + 1, null, Arrays.asList(principal));
				return "redirect:/playground/scribble/active";
			}
		}
		return createGamePage(form, model, locale);
	}


	@RequestMapping("join")
	public String showWaitingGames(@ModelAttribute("join") String join, Model model, Locale locale) throws RestrictionException {
		final Player player = getPrincipal();
		if (log.isDebugEnabled()) {
			log.debug("Loading waiting games for personality: " + player);
		}
		final Player principal = getPrincipal();
		final List<GameProposal<ScribbleSettings>> proposals = new ArrayList<GameProposal<ScribbleSettings>>(proposalManager.getActiveProposals());
		if (log.isDebugEnabled()) {
			log.debug("Found " + proposals.size() + " proposals for personality: " + player);
		}
		model.addAttribute("gamesCount", restrictionManager.getRestriction(principal, "games.active"));
		model.addAttribute("restricted", restrictionManager.isRestricted(principal, "games.active", getActiveGamesCount()));
		model.addAttribute("activeProposals", proposals);
		if (getPrincipal().getMembership().isAdsVisible()) {
			model.addAttribute("advertisementBlock", advertisementManager.getAdvertisementBlock("gameboard", Language.byLocale(locale)));
		}
		return "/content/playground/scribble/join";
	}

	@RequestMapping(value = "join", params = "p")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String joinGameAction(@RequestParam("p") String id, @ModelAttribute("join") String join, Errors errors, Model model, Locale locale) throws BoardManagementException, RestrictionException {
		if (log.isInfoEnabled()) {
			log.info("Join to the game: " + id);
		}
		try {
			final Player principal = getPrincipal();
			restrictionManager.checkRestriction(principal, "games.active", getActiveGamesCount());

			final GameProposal<ScribbleSettings> proposal = proposalManager.attachPlayer(Long.valueOf(id), principal);
			if (proposal == null) {
				errors.reject("game.error.restriction.ready.description", null);
			} else if (proposal.isReady()) {
				final ScribbleBoard board = boardManager.createBoard(proposal.getGameSettings(), proposal.getPlayers());
				return "redirect:/playground/scribble/board?b=" + board.getBoardId();
			}
		} catch (ViolatedRestrictionException e) {
			errors.reject("game.error.restriction." + e.getCode() + ".description", new Object[]{e.getActualValue(), e.getExpectedValue()}, null);
		}
		return showWaitingGames(join, model, locale);
	}

	@RequestMapping("active")
	public String showActiveGames(Model model, Locale locale) {
		final Personality personality = getPersonality();
		if (log.isDebugEnabled()) {
			log.debug("Loading active games for personality: " + personality);
		}
		final Collection<ScribbleBoard> activeBoards = boardManager.getActiveBoards(personality);
		if (log.isDebugEnabled()) {
			log.debug("Found " + activeBoards.size() + " active games for personality: " + personality);
		}
		final Collection<GameProposal<ScribbleSettings>> proposals = proposalManager.getPlayerProposals(personality);
		if (log.isDebugEnabled()) {
			log.debug("Found " + proposals.size() + " proposals for personality: " + personality);
		}
		model.addAttribute("activeBoards", activeBoards);
		model.addAttribute("activeProposals", proposals);
		if (getPrincipal().getMembership().isAdsVisible()) {
			model.addAttribute("advertisementBlock", advertisementManager.getAdvertisementBlock("dashboard", Language.byLocale(locale)));
		}
		return "/content/playground/scribble/active";
	}

	@ResponseBody
	@RequestMapping("cancel")
	public ServiceResponse cancelProposal(@RequestParam("p") String proposal, Model model, Locale locale) {
		final Player player = getPrincipal();
		if (log.isDebugEnabled()) {
			log.debug("Cancel proposal " + proposal + " for player " + player);
		}

		try {
			proposalManager.detachPlayer(Long.valueOf(proposal), player);
			return ServiceResponse.SUCCESS;
		} catch (NumberFormatException ex) {
			return ServiceResponse.failure("format");
		} catch (ViolatedRestrictionException e) {
			return ServiceResponse.failure("active");
		}
	}

	private boolean checkOpponent(String field, String opponent, boolean cpAllowed, List<Personality> players, BindingResult result) {
		final String id = opponent.trim();
		if (!"no".equalsIgnoreCase(id)) {
			if (!id.isEmpty()) {
				try {
					final Long playerId = Long.valueOf(id);
					final Player player1 = playerManager.getPlayer(playerId);
					if (player1 == null) {
						result.rejectValue(field, "game.create.opponents.err.unknown");
					} else if (!cpAllowed && player1 instanceof ComputerPlayer) {
						result.rejectValue(field, "game.create.opponents.err.manyrobots");
					} else {
						players.add(player1);
					}
				} catch (NumberFormatException ex) {
					result.rejectValue(field, "game.create.opponents.err.badid");
				}
			}
			return true;
		}
		return false;
	}

	private int getActiveGamesCount() {
		return searchesEngine.getActiveBoardsCount(getPrincipal()) + proposalManager.getPlayerProposals(getPrincipal()).size();
	}

	@Autowired
	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	@Autowired
	public void setAdvertisementManager(AdvertisementManager advertisementManager) {
		this.advertisementManager = advertisementManager;
	}

	@Autowired
	public void setProposalManager(GameProposalManager<ScribbleSettings> proposalManager) {
		this.proposalManager = proposalManager;
	}

	@Autowired
	public void setBoardManager(ScribbleBoardManager boardManager) {
		this.boardManager = boardManager;
	}

	@Autowired
	public void setRestrictionManager(PlayerRestrictionManager restrictionManager) {
		this.restrictionManager = restrictionManager;
	}

	@Autowired
	public void setSearchesEngine(ScribbleSearchesEngine searchesEngine) {
		this.searchesEngine = searchesEngine;
	}
}
