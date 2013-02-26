package wisematches.server.web.servlet.mvc.playground.scribble.game;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.core.Personality;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.propose.ProposalRelation;
import wisematches.playground.scribble.ScribbleDescription;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.server.web.servlet.mvc.DeprecatedResponse;
import wisematches.server.web.servlet.mvc.UnknownEntityException;
import wisematches.server.web.servlet.mvc.playground.scribble.game.form.PlayerInfoForm;
import wisematches.server.web.servlet.mvc.playground.scribble.game.form.ScribbleInfoForm;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble")
@Deprecated
public class ActiveGameController extends AbstractGameController {
	private static final Log log = LogFactory.getLog("wisematches.server.web.game.active");

	public ActiveGameController() {
	}


	@RequestMapping("active")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String showActiveGames(@RequestParam(value = "p", required = false) Long pid, Model model) throws UnknownEntityException {
		final Personality principal;
		if (pid == null) {
			principal = getPlayer();
		} else {
			principal = personalityManager.getPerson(pid);
		}
		if (principal == null) {
			throw new UnknownEntityException(null, "account");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loading active games for personality: " + principal);
		}
		model.addAttribute("player", principal);

		final Collection<ScribbleDescription> activeBoards = searchManager.searchEntities(principal, ACTIVE_GAMES_CTX, null, null);
		model.addAttribute("activeBoards", activeBoards);
		if (log.isDebugEnabled()) {
			log.debug("Found " + activeBoards.size() + " active games for personality: " + principal);
		}

		if (principal.equals(getPlayer())) {
			final Collection<GameProposal<ScribbleSettings>> proposals =
					proposalManager.searchEntities(principal, ProposalRelation.INVOLVED, null, null);
			model.addAttribute("activeProposals", proposals);
			if (log.isDebugEnabled()) {
				log.debug("Found " + proposals.size() + " proposals for personality: " + principal);
			}
		} else {
			model.addAttribute("activeProposals", Collections.emptyList());
		}
		return "/content/playground/scribble/active";
	}

	@ResponseBody
	@RequestMapping(value = "active.ajax")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public DeprecatedResponse showActiveGamesAjax(@RequestParam(value = "p", required = false) Long pid, Locale locale) throws UnknownEntityException {
		final Personality principal;
		if (pid == null) {
			principal = getPlayer();
		} else {
			principal = personalityManager.getMember(pid);
		}
		if (principal == null) {
			throw new UnknownEntityException(null, "account");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loading active games for personality: " + principal);
		}

		final Collection<ScribbleDescription> activeBoards = searchManager.searchEntities(principal, ACTIVE_GAMES_CTX, null, null);
		final List<ScribbleInfoForm> forms = new ArrayList<>(activeBoards.size());
		for (ScribbleDescription board : activeBoards) {
			final ScribbleSettings settings = board.getSettings();
			final long playerTurn = board.getPlayerTurn() != null ? board.getPlayerTurn().getId() : 0;

			final List<Personality> playersHands = board.getPlayers();
			final PlayerInfoForm[] players = new PlayerInfoForm[playersHands.size()];
			for (int i = 0, playersHandsSize = playersHands.size(); i < playersHandsSize; i++) {
				final Personality player = playersHands.get(i);
				// TODO: commented
/*
				final Personality player = personalityManager.getMember(hand.getPlayerId());
				players[i] = new PlayerInfoForm(player.getId(),
						player.getNickname(),
						player.getMembership().name(),
						playerStateManager.isPlayerOnline(player),
						hand.getPoints());
*/
			}
			final String elapsedTime = messageSource.formatRemainedTime(board, locale);
			forms.add(new ScribbleInfoForm(board.getBoardId(), settings, elapsedTime, playerTurn, players));
		}
		return DeprecatedResponse.success(null, "boards", forms);
	}
}
