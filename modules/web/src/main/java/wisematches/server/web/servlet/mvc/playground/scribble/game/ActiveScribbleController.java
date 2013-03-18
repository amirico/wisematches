package wisematches.server.web.servlet.mvc.playground.scribble.game;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.core.Personality;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.propose.ProposalRelation;
import wisematches.playground.scribble.ScribbleDescription;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.server.web.servlet.mvc.UnknownEntityException;
import wisematches.server.web.servlet.mvc.playground.scribble.AbstractScribbleController;
import wisematches.server.web.servlet.sdo.ServiceResponse;
import wisematches.server.web.servlet.sdo.scribble.game.ActiveGamesInfo;
import wisematches.server.web.servlet.sdo.scribble.game.GameInfo;
import wisematches.server.web.servlet.sdo.scribble.game.ProposalInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble")
public class ActiveScribbleController extends AbstractScribbleController {
	private static final Logger log = LoggerFactory.getLogger("wisematches.web.mvc.ActiveGameController");

	public ActiveScribbleController() {
	}

	@RequestMapping("active")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String activeGamesPage(@RequestParam(value = "p", required = false) Long pid, Model model) throws UnknownEntityException {
		final Personality principal;
		if (pid == null) {
			principal = getPrincipal();
		} else {
			principal = personalityManager.getPerson(pid);
		}
		if (principal == null) {
			throw new UnknownEntityException(null, "account");
		}

		log.debug("Loading active games for personality: {}", principal);
		model.addAttribute("player", principal);

		final Collection<ScribbleDescription> activeBoards = searchManager.searchEntities(principal, ACTIVE_GAMES_CTX, null, null);
		model.addAttribute("boards", activeBoards);
		log.debug("Found {} active games for personality: {}", activeBoards.size(), principal);

		if (principal.equals(getPrincipal())) {
			final Collection<GameProposal<ScribbleSettings>> proposals =
					proposalManager.searchEntities(principal, ProposalRelation.INVOLVED, null, null);
			model.addAttribute("proposals", proposals);
			log.debug("Found {} proposals for personality: {}", proposals.size(), principal);
		}
		return "/content/playground/scribble/active";
	}

	@RequestMapping(value = "active.ajax")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ServiceResponse activeGamesService(@RequestParam(value = "p", required = false) Long pid, Locale locale) throws UnknownEntityException {
		final Personality principal;
		if (pid == null) {
			principal = getPrincipal();
		} else {
			principal = personalityManager.getMember(pid);
		}
		if (principal == null) {
			throw new UnknownEntityException(null, "account");
		}
		final ActiveGamesInfo activeGames = createActiveGames(principal, locale);
		log.debug("Loading active games for personality: {}", principal);
		return responseFactory.success(activeGames);
	}

	private ActiveGamesInfo createActiveGames(Personality principal, Locale locale) {
		Collection<ScribbleDescription> activeBoards = searchManager.searchEntities(principal, ACTIVE_GAMES_CTX, null, null);
		List<GameInfo> boards = new ArrayList<>(activeBoards.size());
		for (ScribbleDescription board : activeBoards) {
			boards.add(new GameInfo(board, messageSource, playerStateManager, locale));
		}

		Collection<ProposalInfo> proposalInfos = null;
		if (principal.equals(getPrincipal())) {
			final List<GameProposal<ScribbleSettings>> proposals = proposalManager.searchEntities(principal, ProposalRelation.INVOLVED, null, null);
			proposalInfos = new ArrayList<>(proposals.size());
			for (GameProposal<ScribbleSettings> proposal : proposals) {
				proposalInfos.add(new ProposalInfo(proposal, null));
			}
		}
		return new ActiveGamesInfo(boards, proposalInfos);
	}
}
