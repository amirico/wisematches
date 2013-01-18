package wisematches.server.web.controllers.playground.scribble.controller.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.core.personality.Player;
import wisematches.core.personality.PlayerManager;
import wisematches.core.personality.proprietary.guest.GuestPlayer;
import wisematches.playground.GameState;
import wisematches.playground.propose.GameProposalManager;
import wisematches.playground.propose.ProposalRelation;
import wisematches.playground.scribble.ScribbleBoardManager;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.playground.tracking.StatisticManager;
import wisematches.server.web.controllers.WisematchesController;
import wisematches.server.web.i18n.GameMessageSource;
import wisematches.server.web.services.state.PlayerStateManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble")
public class AbstractGameController extends WisematchesController {
	protected PlayerManager playerManager;
	protected GameMessageSource messageSource;
	protected ScribbleBoardManager boardManager;
	protected PlayerStateManager playerStateManager;
	protected StatisticManager playerStatisticManager;
	protected GameProposalManager<ScribbleSettings> proposalManager;

	public AbstractGameController() {
	}

	protected int getActiveGamesCount(Player principal) {
		int activeGames;
		if (!GuestPlayer.isGuestPlayer(principal)) {
			activeGames = playerStatisticManager.getPlayerStatistic(principal).getActiveGames();
		} else {
			activeGames = boardManager.getTotalCount(principal, GameState.ACTIVE);
		}
		return activeGames + proposalManager.getTotalCount(principal, ProposalRelation.INVOLVED);
	}

	protected int getFinishedGamesCount(Player principal) {
		return playerStatisticManager.getPlayerStatistic(principal).getFinishedGames();
	}

	@Autowired
	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	@Autowired
	public void setMessageSource(GameMessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Autowired
	public void setBoardManager(ScribbleBoardManager boardManager) {
		this.boardManager = boardManager;
	}

	@Autowired
	public void setPlayerStateManager(PlayerStateManager playerStateManager) {
		this.playerStateManager = playerStateManager;
	}

	@Autowired
	public void setPlayerStatisticManager(StatisticManager playerStatisticManager) {
		this.playerStatisticManager = playerStatisticManager;
	}

	@Autowired
	public void setProposalManager(GameProposalManager<ScribbleSettings> proposalManager) {
		this.proposalManager = proposalManager;
	}
}
