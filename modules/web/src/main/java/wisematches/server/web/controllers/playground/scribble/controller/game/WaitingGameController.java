package wisematches.server.web.controllers.playground.scribble.controller.game;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.core.Personality;
import wisematches.core.personality.Player;
import wisematches.playground.BoardCreationException;
import wisematches.playground.blacklist.BlacklistManager;
import wisematches.playground.criteria.CriterionViolation;
import wisematches.playground.criteria.ViolatedCriteriaException;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.propose.ProposalRelation;
import wisematches.playground.restriction.Restriction;
import wisematches.playground.restriction.RestrictionManager;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.playground.scribble.view.GameProposalView;
import wisematches.server.web.controllers.playground.scribble.view.WaitingGamesView;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble")
public class WaitingGameController extends AbstractGameController {
	private BlacklistManager blacklistManager;
	private RestrictionManager restrictionManager;

	private static final Log log = LogFactory.getLog("wisematches.server.web.game.waiting");

	public WaitingGameController() {
	}

	@RequestMapping("join")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String showWaitingGames(Model model) {
		final Player principal = getPrincipal();
		if (log.isDebugEnabled()) {
			log.debug("Loading waiting games for personality: " + principal);
		}
		model.addAttribute("waitingGames", createWaitingGamesView(principal));
		return "/content/playground/scribble/join";
	}

	@ResponseBody
	@RequestMapping("join.ajax")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ServiceResponse showWaitingGamesAjax() {
		final Player principal = getPrincipal();
		if (log.isDebugEnabled()) {
			log.debug("Loading waiting games for personality: " + principal);
		}
		return ServiceResponse.success(null, "waitingGames", createWaitingGamesView(principal));
	}

	@ResponseBody
	@RequestMapping("accept.ajax")
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public ServiceResponse acceptProposalAjax(@RequestParam("p") long proposal, Locale locale) {
		final Player player = getPrincipal();
		if (log.isDebugEnabled()) {
			log.debug("Cancel proposal " + proposal + " for player " + player);
		}

		try {
			final CriterionViolation violation = checkGlobalViolation(player);
			if (violation != null) {
				throw new ViolatedCriteriaException(violation);
			}

			final GameProposal<ScribbleSettings> gameProposal = proposalManager.accept(proposal, player);
			if (gameProposal == null) {
				return ServiceResponse.failure(messageSource.getMessage("game.join.err.game.unknown.description", locale));
			} else if (gameProposal.isReady()) {
				final ScribbleBoard board = boardManager.createBoard(gameProposal.getSettings(), gameProposal.getPlayers());
				return ServiceResponse.success(null, "board", board.getBoardId());
			}
			return ServiceResponse.SUCCESS;
		} catch (ViolatedCriteriaException e) {
			final CriterionViolation violation = e.getViolatedCriterion();
			if (violation != null) {
				return ServiceResponse.failure(messageSource.formatViolation(violation, locale, false));
			}
			return ServiceResponse.failure(messageSource.getMessage("game.join.err.system.description", locale));
		} catch (BoardCreationException e) {
			return ServiceResponse.failure(messageSource.getMessage("game.join.err.system.description", locale));
		}
	}

	@ResponseBody
	@RequestMapping("decline.ajax")
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public ServiceResponse declineProposalAjax(@RequestParam("p") long proposal, Locale locale) {
		final Player player = getPrincipal();
		if (log.isDebugEnabled()) {
			log.debug("Cancel proposal " + proposal + " for player " + player);
		}

		try {
			if (proposalManager.reject(proposal, player) == null) {
				return ServiceResponse.failure(messageSource.getMessage("game.join.err.game.unknown.description", locale));
			}
			return ServiceResponse.SUCCESS;
		} catch (ViolatedCriteriaException e) {
			final CriterionViolation violation = e.getViolatedCriterion();
			if (violation != null) {
				return ServiceResponse.failure(messageSource.formatViolation(violation, locale, false));
			}
			return ServiceResponse.failure(messageSource.getMessage("game.join.err.system.description", locale));
		}
	}

	private WaitingGamesView createWaitingGamesView(Player principal) {
		final CriterionViolation globalViolation = checkGlobalViolation(principal);
		final List<GameProposalView> activeProposals = new ArrayList<>();
		for (GameProposal<ScribbleSettings> proposal : proposalManager.searchEntities(principal, ProposalRelation.AVAILABLE, null, null, null)) {
			if (globalViolation == null) {
				activeProposals.add(new GameProposalView(proposal, checkProposalViolation(proposal)));
			} else {
				activeProposals.add(new GameProposalView(proposal, Collections.singleton(globalViolation)));
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("Found " + activeProposals.size() + " proposals for personality: " + principal);
		}
		return new WaitingGamesView(globalViolation, activeProposals);
	}

	private CriterionViolation checkGlobalViolation(Player player) {
		CriterionViolation globalViolation = null;
		if (player.getPlayerType().isGuest()) {
			globalViolation = new CriterionViolation("guest", null);
		}

		if (globalViolation == null) {
			final int finishedGamesCount = getFinishedGamesCount(player);
			if (finishedGamesCount < 1) {
				globalViolation = new CriterionViolation("newbie", finishedGamesCount, 1);
			}
		}

		if (globalViolation == null) {
			final int activeGamesCount = getActiveGamesCount(player);
			final Restriction restriction = restrictionManager.validateRestriction(player, "games.active", activeGamesCount);
			if (restriction != null) {
				globalViolation = new CriterionViolation("restricted", restriction.getViolation(), restriction.getThreshold());
			}
		}
		return globalViolation;
	}

	private Collection<CriterionViolation> checkProposalViolation(GameProposal<ScribbleSettings> proposal) {
		final Player principal = getPrincipal();

		boolean blacklisted = false;
		for (Iterator<Personality> iterator = proposal.getPlayers().iterator(); iterator.hasNext() && !blacklisted; ) {
			Personality personality = iterator.next();
			if (personality != null && !principal.equals(personality)) {
				blacklisted = blacklistManager.isBlacklisted(personality, principal);
			}
		}
		if (blacklisted) {
			return Collections.singleton(new CriterionViolation("blacklisted", true));
		}
		return proposalManager.validate(proposal.getId(), principal);
	}


	@Autowired
	public void setBlacklistManager(BlacklistManager blacklistManager) {
		this.blacklistManager = blacklistManager;
	}

	@Autowired
	public void setRestrictionManager(RestrictionManager restrictionManager) {
		this.restrictionManager = restrictionManager;
	}
}
