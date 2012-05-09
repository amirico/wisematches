package wisematches.server.web.controllers.playground.scribble.controller.game;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.personality.player.Player;
import wisematches.playground.GameState;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.propose.ProposalRelation;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribblePlayerHand;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.UnknownEntityException;
import wisematches.server.web.controllers.playground.scribble.form.PlayerInfoForm;
import wisematches.server.web.controllers.playground.scribble.form.ScribbleInfoForm;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble")
public class ActiveGameController extends AbstractGameController {
    private static final Log log = LogFactory.getLog("wisematches.server.web.game.active");

    public ActiveGameController() {
    }


    @RequestMapping("active")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String showActiveGames(@RequestParam(value = "p", required = false) Long pid, Model model) throws UnknownEntityException {
        final Player principal;
        if (pid == null) {
            principal = getPrincipal();
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

        final Collection<ScribbleBoard> activeBoards = boardManager.searchEntities(principal, GameState.ACTIVE, null, null, null);
        model.addAttribute("activeBoards", activeBoards);
        if (log.isDebugEnabled()) {
            log.debug("Found " + activeBoards.size() + " active games for personality: " + principal);
        }

        if (principal == getPrincipal()) {
            final Collection<GameProposal<ScribbleSettings>> proposals =
                    proposalManager.searchEntities(principal, ProposalRelation.INVOLVED, null, null, null);
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
    public ServiceResponse showActiveGamesAjax(@RequestParam(value = "p", required = false) Long pid, Locale locale) throws UnknownEntityException {
        final Player principal;
        if (pid == null) {
            principal = getPrincipal();
        } else {
            principal = playerManager.getPlayer(pid);
        }
        if (principal == null) {
            throw new UnknownEntityException(null, "account");
        }
        if (log.isDebugEnabled()) {
            log.debug("Loading active games for personality: " + principal);
        }

        final Collection<ScribbleBoard> activeBoards = boardManager.searchEntities(principal, GameState.ACTIVE, null, null, null);
        final List<ScribbleInfoForm> forms = new ArrayList<ScribbleInfoForm>(activeBoards.size());
        for (ScribbleBoard board : activeBoards) {
            final ScribbleSettings settings = board.getGameSettings();
            final long playerTurn = board.getPlayerTurn() != null ? board.getPlayerTurn().getPlayerId() : 0;

            final List<ScribblePlayerHand> playersHands = board.getPlayersHands();
            final PlayerInfoForm[] players = new PlayerInfoForm[playersHands.size()];
            for (int i = 0, playersHandsSize = playersHands.size(); i < playersHandsSize; i++) {
                final ScribblePlayerHand hand = playersHands.get(i);
                final Player player = playerManager.getPlayer(hand.getPlayerId());
                players[i] = new PlayerInfoForm(player.getId(),
                        player.getNickname(),
                        player.getMembership().name(),
                        playerStateManager.isPlayerOnline(player),
                        hand.getPoints());
            }
            final String elapsedTime = messageSource.formatRemainedTime(board, locale);
            forms.add(new ScribbleInfoForm(board.getBoardId(), settings, elapsedTime, playerTurn, players));
        }
        return ServiceResponse.success(null, "boards", forms);
    }
}
