package wisematches.server.web.servlet.mvc.playground.scribble.game;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.core.Player;
import wisematches.playground.BoardCreationException;
import wisematches.playground.propose.CriterionViolation;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.propose.ProposalRelation;
import wisematches.playground.propose.criteria.ViolatedCriteriaException;
import wisematches.playground.restriction.Restriction;
import wisematches.playground.restriction.RestrictionManager;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.server.services.relations.blacklist.BlacklistManager;
import wisematches.server.web.servlet.mvc.DeprecatedResponse;
import wisematches.server.web.servlet.mvc.playground.scribble.AbstractScribbleController;
import wisematches.server.web.servlet.mvc.playground.scribble.game.form.GameProposalForm;
import wisematches.server.web.servlet.mvc.playground.scribble.game.form.WaitingGamesForm;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble")
@Deprecated
public class WaitingScribbleController extends AbstractScribbleController {
	private BlacklistManager blacklistManager;
	private RestrictionManager restrictionManager;

	private static final Logger log = LoggerFactory.getLogger("wisematches.web.mvc.AccountController");

	public WaitingScribbleController() {
	}

	@RequestMapping("join")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String showWaitingGames(Model model) {
		final Player principal = getPrincipal();
		log.debug("Loading waiting games for personality: {}", principal);
		model.addAttribute("waitingGames", createWaitingGamesView(principal));
		return "/content/playground/scribble/join";
	}

	@ResponseBody
	@RequestMapping("join.ajax")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public DeprecatedResponse showWaitingGamesAjax() {
		final Player principal = getPrincipal();
		log.debug("Loading waiting games for personality: {}", principal);
		return DeprecatedResponse.success(null, "waitingGames", createWaitingGamesView(principal));
	}

	@ResponseBody
	@RequestMapping("accept.ajax")
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public DeprecatedResponse acceptProposalAjax(@RequestParam("p") long proposal, Locale locale) {
		final Player player = getPrincipal();
		log.debug("Cancel proposal {} for player {}", proposal, player);

		try {
			final CriterionViolation violation = checkGlobalViolation(player);
			if (violation != null) {
				throw new ViolatedCriteriaException(violation);
			}

			final GameProposal<ScribbleSettings> gameProposal = proposalManager.accept(proposal, player);
			if (gameProposal == null) {
				return DeprecatedResponse.failure(messageSource.getMessage("game.join.err.game.unknown.description", locale));
			} else if (gameProposal.isReady()) {
				final ScribbleBoard board = playManager.createBoard(gameProposal.getSettings(), gameProposal.getPlayers(), null);
				return DeprecatedResponse.success(null, "board", board.getBoardId());
			}
			return DeprecatedResponse.SUCCESS;
		} catch (ViolatedCriteriaException e) {
			final CriterionViolation violation = e.getViolatedCriterion();
			if (violation != null) {
				return DeprecatedResponse.failure(messageSource.formatViolation(violation, locale, false));
			}
			return DeprecatedResponse.failure(messageSource.getMessage("game.join.err.system.description", locale));
		} catch (BoardCreationException e) {
			return DeprecatedResponse.failure(messageSource.getMessage("game.join.err.system.description", locale));
		}
	}

	@ResponseBody
	@RequestMapping("decline.ajax")
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public DeprecatedResponse declineProposalAjax(@RequestParam("p") long proposal, Locale locale) {
		final Player player = getPrincipal();
		log.debug("Cancel proposal {} for player {}", proposal, player);

		try {
			if (proposalManager.reject(proposal, player) == null) {
				return DeprecatedResponse.failure(messageSource.getMessage("game.join.err.game.unknown.description", locale));
			}
			return DeprecatedResponse.SUCCESS;
		} catch (ViolatedCriteriaException e) {
			final CriterionViolation violation = e.getViolatedCriterion();
			if (violation != null) {
				return DeprecatedResponse.failure(messageSource.formatViolation(violation, locale, false));
			}
			return DeprecatedResponse.failure(messageSource.getMessage("game.join.err.system.description", locale));
		}
	}

	private WaitingGamesForm createWaitingGamesView(Player principal) {
		final CriterionViolation globalViolation = checkGlobalViolation(principal);
		final List<GameProposalForm> activeProposals = new ArrayList<>();
		for (GameProposal<ScribbleSettings> proposal : proposalManager.searchEntities(principal, ProposalRelation.AVAILABLE, null, null)) {
			if (globalViolation == null) {
				activeProposals.add(new GameProposalForm(proposal, checkProposalViolation(proposal)));
			} else {
				activeProposals.add(new GameProposalForm(proposal, Collections.singleton(globalViolation)));
			}
		}
		log.debug("Found {} proposals for personality: {}", activeProposals.size(), principal);
		return new WaitingGamesForm(globalViolation, activeProposals);
	}

	private CriterionViolation checkGlobalViolation(Player player) {
		CriterionViolation globalViolation = null;
/*
		if (player instanceof Visitor) {
			globalViolation = new CriterionViolation("guest", null);
		}
*/

//		if (globalViolation == null) {
		final int finishedGamesCount = getFinishedGamesCount(player);
		if (finishedGamesCount < 1) {
			globalViolation = new CriterionViolation("newbie", finishedGamesCount, 1);
		}
//		}

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
		for (Iterator<Player> iterator = proposal.getPlayers().iterator(); iterator.hasNext() && !blacklisted; ) {
			Player personality = iterator.next();
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
