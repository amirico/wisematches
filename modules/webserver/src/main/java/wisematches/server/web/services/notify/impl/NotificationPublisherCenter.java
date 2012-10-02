package wisematches.server.web.services.notify.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.personality.player.PlayerManager;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.playground.*;
import wisematches.playground.expiration.ExpirationListener;
import wisematches.playground.message.Message;
import wisematches.playground.message.MessageListener;
import wisematches.playground.message.MessageManager;
import wisematches.playground.propose.*;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.playground.scribble.expiration.ScribbleExpirationManager;
import wisematches.playground.scribble.expiration.ScribbleExpirationType;
import wisematches.server.web.services.notify.NotificationSender;
import wisematches.server.web.services.notify.NotificationDistributor;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class NotificationPublisherCenter {
	private MessageManager manager;
	private BoardManager boardManager;
	private PlayerManager playerManager;
	private GameProposalManager proposalManager;
	private ScribbleExpirationManager scribbleExpirationManager;
	private ProposalExpirationManager<ScribbleSettings> proposalExpirationManager;

	private NotificationDistributor notificationDistributor;

	private final TheNotificationListener notificationListener = new TheNotificationListener();
	private final TheScribbleGameExpirationListener gameExpirationListener = new TheScribbleGameExpirationListener();
	private final TheScribbleProposalExpirationListener proposalExpirationListener = new TheScribbleProposalExpirationListener();

	private static final Log log = LogFactory.getLog("wisematches.server.notice.center");

	public NotificationPublisherCenter() {
	}

	protected void processNotification(long person, String code, Object context) {
		final Player player = playerManager.getPlayer(person);
		if (player instanceof MemberPlayer) {
			fireNotification(code, (MemberPlayer) player, context);
		}
	}

	protected void processNotification(Personality person, String code, Object context) {
		final Player player = playerManager.getPlayer(person);
		if (player instanceof MemberPlayer) {
			fireNotification(code, (MemberPlayer) player, context);
		}
	}

	private void fireNotification(String code, MemberPlayer player, Object context) {
		if (log.isInfoEnabled()) {
			log.info("Fire notification " + code + " to person " + player);
		}
		notificationDistributor.raiseNotification(code, player, NotificationSender.GAME, context);
	}

	public void setMessageManager(MessageManager manager) {
		if (this.manager != null) {
			this.manager.removeMessageListener(notificationListener);
		}

		this.manager = manager;

		if (this.manager != null) {
			this.manager.addMessageListener(notificationListener);
		}
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public void setBoardManager(BoardManager boardManager) {
		if (this.boardManager != null) {
			this.boardManager.removeBoardStateListener(notificationListener);
		}
		this.boardManager = boardManager;
		if (this.boardManager != null) {
			this.boardManager.addBoardStateListener(notificationListener);
		}
	}

	public void setProposalManager(GameProposalManager proposalManager) {
		if (this.proposalManager != null) {
			this.proposalManager.removeGameProposalListener(notificationListener);
		}

		this.proposalManager = proposalManager;

		if (this.proposalManager != null) {
			this.proposalManager.addGameProposalListener(notificationListener);
		}
	}

	public void setScribbleExpirationManager(ScribbleExpirationManager expirationManager) {
		if (this.scribbleExpirationManager != null) {
			this.scribbleExpirationManager.removeExpirationListener(gameExpirationListener);
		}

		this.scribbleExpirationManager = expirationManager;

		if (this.scribbleExpirationManager != null) {
			this.scribbleExpirationManager.addExpirationListener(gameExpirationListener);
		}
	}

	public void setProposalExpirationManager(ProposalExpirationManager<ScribbleSettings> proposalExpirationManager) {
		if (this.proposalExpirationManager != null) {
			this.proposalExpirationManager.removeExpirationListener(proposalExpirationListener);
		}

		this.proposalExpirationManager = proposalExpirationManager;

		if (this.proposalExpirationManager != null) {
			this.proposalExpirationManager.addExpirationListener(proposalExpirationListener);
		}
	}

	public void setNotificationDistributor(NotificationDistributor notificationDistributor) {
		this.notificationDistributor = notificationDistributor;
	}

	private class TheScribbleGameExpirationListener implements ExpirationListener<Long, ScribbleExpirationType> {
		private TheScribbleGameExpirationListener() {
		}

		@Override
		public void expirationTriggered(Long boardId, ScribbleExpirationType type) {
			try {
				final GameBoard board = boardManager.openBoard(boardId);
				if (board != null) {
					final GamePlayerHand hand = board.getPlayerTurn();
					if (hand != null) {
						Map<String, Object> c = new HashMap<String, Object>();
						c.put("board", board);
						c.put("expirationType", type);
						processNotification(Personality.person(hand.getPlayerId()), type.getCode(), c);
					}
				}
			} catch (BoardLoadingException ignored) {
			}
		}
	}

	private class TheScribbleProposalExpirationListener implements ExpirationListener<Long, ProposalExpirationType> {
		private TheScribbleProposalExpirationListener() {
		}

		@Override
		public void expirationTriggered(Long entity, ProposalExpirationType type) {
			final GameProposal<?> proposal = proposalManager.getProposal(entity);
			if (proposal == null || proposal.isReady()) {
				return;
			}
			final List<Personality> waiting = new ArrayList<Personality>(proposal.getPlayers());
			waiting.removeAll(proposal.getJoinedPlayers());
			if (!waiting.isEmpty()) {
				Map<String, Object> c = new HashMap<String, Object>();
				c.put("proposal", proposal);
				c.put("expirationType", type);
				for (Personality personality : waiting) {
					processNotification(personality, type.getCode(), c);
				}
			}
		}
	}

	private class TheNotificationListener implements BoardStateListener, MessageListener, GameProposalListener {
		private TheNotificationListener() {
		}

		@Override
		public void gameProposalInitiated(GameProposal<? extends GameSettings> proposal) {
			List<Personality> players = proposal.getPlayers();
			for (Personality player : players) {
				if (player == null || proposal.getInitiator().equals(player) || RobotPlayer.isRobotPlayer(player)) {
					continue;
				}
				processNotification(player, "playground.challenge.initiated", Collections.singletonMap("proposal", proposal));
			}
		}

		@Override
		public void gameProposalUpdated(GameProposal<? extends GameSettings> proposal, Personality player, ProposalDirective directive) {
		}

		@Override
		public void gameProposalFinalized(GameProposal<? extends GameSettings> proposal, ProposalResolution resolution, Personality player) {
			Map<String, Object> c = new HashMap<String, Object>();
			c.put("proposal", proposal);
			c.put("player", player);
			c.put("resolution", resolution);

			for (Personality personality : proposal.getJoinedPlayers()) {
				if (personality.equals(player)) {
					continue;
				}
				if (resolution == ProposalResolution.REJECTED) {
					processNotification(personality, "playground.challenge.rejected", c);
				} else if (resolution == ProposalResolution.REPUDIATED) {
					processNotification(personality, "playground.challenge.repudiated", c);
				} else if (resolution == ProposalResolution.TERMINATED) {
					processNotification(personality, "playground.challenge.terminated", c);
				}
			}
		}

		@Override
		public void gameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board) {
			final Collection<? extends GamePlayerHand> playersHands = board.getPlayersHands();
			for (GamePlayerHand hand : playersHands) {
				processNotification(hand.getPlayerId(), "playground.game.started", Collections.singletonMap("board", board));
			}
		}

		@Override
		public void gameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move, GameMoveScore moveScore) {
			final GamePlayerHand playerTurn = board.getPlayerTurn();
			if (playerTurn != null) {
				final Map<String, Object> map = new HashMap<String, Object>();
				map.put("board", board);
				map.put("changes", board.getGameChanges(playerTurn.getPlayerId()));
				processNotification(Personality.person(playerTurn.getPlayerId()), "playground.game.turn", map);
			}
		}

		@Override
		public void gameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameResolution resolution, Collection<? extends GamePlayerHand> winners) {
			final Collection<? extends GamePlayerHand> playersHands = board.getPlayersHands();
			final Map<String, Object> map = new HashMap<String, Object>();
			map.put("board", board);
			map.put("winners", winners);
			map.put("resolution", resolution);
			for (GamePlayerHand hand : playersHands) {
				processNotification(Personality.person(hand.getPlayerId()), "playground.game.finished", map);
			}
		}

		@Override
		public void messageSent(Message message, boolean quite) {
			if (!quite) {
				processNotification(Personality.person(message.getRecipient()), "playground.message.received", Collections.singletonMap("message", message));
			}
		}
	}
}
