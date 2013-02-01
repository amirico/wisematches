package wisematches.server.web.controllers.playground.scribble.controller.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.core.Personality;
import wisematches.core.personality.player.MemberPlayerManager;
import wisematches.core.personality.proprietary.guest.GuestPlayer;
import wisematches.playground.propose.GameProposalManager;
import wisematches.playground.propose.ProposalRelation;
import wisematches.playground.scribble.ScribblePlayManager;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.playground.search.GameState;
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
	protected MemberPlayerManager playerManager;
	protected GameMessageSource messageSource;
	protected ScribblePlayManager boardManager;
	protected PlayerStateManager playerStateManager;
	protected StatisticManager playerStatisticManager;
	protected GameProposalManager<ScribbleSettings> proposalManager;

	public AbstractGameController() {
	}

	protected int getActiveGamesCount(Personality principal) {
		int activeGames;
		if (!GuestPlayer.isGuestPlayer(principal)) {
			activeGames = playerStatisticManager.getStatistic(principal).getActiveGames();
		} else {
			activeGames = boardManager.getTotalCount(principal, GameState.ACTIVE);
		}
		return activeGames + proposalManager.getTotalCount(principal, ProposalRelation.INVOLVED);
	}

	protected int getFinishedGamesCount(Personality principal) {
		return playerStatisticManager.getStatistic(principal).getFinishedGames();
	}

	@Autowired
	public void setPlayerManager(MemberPlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	@Autowired
	public void setMessageSource(GameMessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Autowired
	public void setBoardManager(ScribblePlayManager boardManager) {
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