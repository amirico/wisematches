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
import wisematches.core.Visitor;
import wisematches.playground.BoardCreationException;
import wisematches.playground.propose.CriterionViolation;
import wisematches.playground.propose.CriterionViolationException;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.propose.ProposalRelation;
import wisematches.playground.restriction.Restriction;
import wisematches.playground.restriction.RestrictionManager;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.server.services.relations.blacklist.BlacklistManager;
import wisematches.server.web.servlet.mvc.playground.scribble.AbstractScribbleController;
import wisematches.server.web.servlet.sdo.ServiceResponse;
import wisematches.server.web.servlet.sdo.scribble.game.ProposalInfo;
import wisematches.server.web.servlet.sdo.scribble.game.WaitingGamesInfo;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble")
public class WaitingScribbleController extends AbstractScribbleController {
	private BlacklistManager blacklistManager;
	private RestrictionManager restrictionManager;

	private static final Logger log = LoggerFactory.getLogger("wisematches.web.mvc.AccountController");

	public WaitingScribbleController() {
	}

	@RequestMapping("join")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String activeProposalsPage(Model model) {
		final Player principal = getPrincipal();
		log.debug("Loading waiting games for personality: {}", principal);

		final Collection<CriterionViolation> globalViolations = checkGlobalViolations(principal);
		model.addAttribute("globalViolations", globalViolations);

		Collection<Map.Entry<GameProposal<ScribbleSettings>, Collection<CriterionViolation>>> waitingGames = new ArrayList<>();
		final List<GameProposal<ScribbleSettings>> proposals = proposalManager.searchEntities(principal, ProposalRelation.AVAILABLE, null, null);
		for (GameProposal<ScribbleSettings> proposal : proposals) {
			Collection<CriterionViolation> criterionViolations;
			if (globalViolations != null && !globalViolations.isEmpty()) {
				criterionViolations = globalViolations;
			} else {
				criterionViolations = checkProposalViolation(proposal);
			}

			waitingGames.add(new AbstractMap.SimpleImmutableEntry<>(proposal, criterionViolations));
		}
		model.addAttribute("proposals", waitingGames);

		return "/content/playground/scribble/join";
	}

	@RequestMapping("join.ajax")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ServiceResponse activeProposalsService() {
		final Player principal = getPrincipal();
		log.debug("Loading waiting games for personality: {}", principal);
		return responseFactory.success(createProposals(principal));
	}

	@ResponseBody
	@RequestMapping("accept.ajax")
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public ServiceResponse acceptProposalAjax(@RequestParam("p") long proposal, Locale locale) {
		final Player player = getPrincipal();
		log.debug("Cancel proposal {} for player {}", proposal, player);

		try {
			final Collection<CriterionViolation> violations = checkGlobalViolations(player);
			if (violations != null) {
				throw new CriterionViolationException(violations);
			}

			final GameProposal<ScribbleSettings> gameProposal = proposalManager.accept(proposal, player);
			if (gameProposal == null) {
				return responseFactory.failure("game.join.err.game.unknown.description", locale);
			} else if (gameProposal.isReady()) {
				final ScribbleBoard board = playManager.createBoard(gameProposal.getSettings(), gameProposal.getPlayers(), null);
				return responseFactory.success(board.getBoardId());
			}
			return responseFactory.success();
		} catch (CriterionViolationException e) {
			final CriterionViolation violation = e.getCriterion();
			if (violation != null) {
				return responseFactory.failure("game.join.err." + violation.getCode() + ".description",
						new Object[]{violation.getExpected(), violation.getReceived()}, locale);
			}
			return responseFactory.failure("game.join.err.system.description", locale);
		} catch (BoardCreationException e) {
			return responseFactory.failure("game.join.err.system.description", locale);
		}
	}

	@ResponseBody
	@RequestMapping("decline.ajax")
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public ServiceResponse declineProposalAjax(@RequestParam("p") long proposal, Locale locale) {
		final Player player = getPrincipal();
		log.debug("Cancel proposal {} for player {}", proposal, player);

		try {
			if (proposalManager.reject(proposal, player) == null) {
				return responseFactory.failure("game.join.err.game.unknown.description", locale);
			}
			return responseFactory.success();
		} catch (CriterionViolationException e) {
			final CriterionViolation violation = e.getCriterion();
			if (violation != null) {
				return responseFactory.failure("game.join.err." + violation.getCode() + ".description",
						new Object[]{violation.getExpected(), violation.getReceived()}, locale);
			}
			return responseFactory.failure("game.join.err.system.description", locale);
		}
	}

	private WaitingGamesInfo createProposals(Player principal) {
		final Collection<CriterionViolation> globalViolations = checkGlobalViolations(principal);
		final List<ProposalInfo> activeProposals = new ArrayList<>();
		for (GameProposal<ScribbleSettings> proposal : proposalManager.searchEntities(principal, ProposalRelation.AVAILABLE, null, null)) {
			if (globalViolations == null) {
				activeProposals.add(new ProposalInfo(proposal, checkProposalViolation(proposal)));
			} else {
				activeProposals.add(new ProposalInfo(proposal, globalViolations));
			}
		}
		log.debug("Found {} proposals for personality: {}", activeProposals.size(), principal);
		return new WaitingGamesInfo(activeProposals, globalViolations);
	}

	private Collection<CriterionViolation> checkGlobalViolations(Player player) {
		if (player instanceof Visitor) {
			return Collections.singleton(new CriterionViolation("guest", null));
		}

		final int finishedGamesCount = getFinishedGamesCount(player);
		if (finishedGamesCount < 1) {
			return Collections.singleton(new CriterionViolation("newbie", finishedGamesCount, 1));
		}

		final int activeGamesCount = getActiveGamesCount(player);
		final Restriction restriction = restrictionManager.validateRestriction(player, "games.active", activeGamesCount);
		if (restriction != null) {
			return Collections.singleton(new CriterionViolation("restricted", restriction.getViolation(), restriction.getThreshold()));
		}
		return null;
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
