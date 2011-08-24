package wisematches.server.web.services.notice.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.personality.player.PlayerManager;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.playground.*;
import wisematches.playground.expiration.GameExpirationListener;
import wisematches.playground.expiration.GameExpirationManager;
import wisematches.playground.expiration.GameExpirationType;
import wisematches.playground.message.Message;
import wisematches.playground.message.MessageListener;
import wisematches.playground.propose.ChallengeGameProposal;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.propose.GameProposalListener;
import wisematches.server.web.services.notice.Notification;
import wisematches.server.web.services.notice.NotificationManager;
import wisematches.server.web.services.notice.NotificationPublisher;
import wisematches.server.web.services.notify.settings.NotificationDescription;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class NotificationPublishCenter {
	private BoardManager boardManager;
	private PlayerManager playerManager;
	private NotificationManager notificationManager;
	private GameExpirationManager expirationManager;

	private final TheNotificationListener notificationListener = new TheNotificationListener();
	private final Collection<NotificationPublisher> publishers = new ArrayList<NotificationPublisher>();

	private static final Log log = LogFactory.getLog("wisematches.server.notice.center");

	public NotificationPublishCenter() {
	}

	private void processNotification(long person, NotificationDescription description, Object context) {
		final Player player = playerManager.getPlayer(person);
		if (player instanceof MemberPlayer) {
			fireNotification(new Notification((MemberPlayer) player, description, context));
		}
	}

	private void processNotification(Personality person, NotificationDescription description, Object context) {
		final Player player = playerManager.getPlayer(person);
		if (player instanceof MemberPlayer) {
			fireNotification(new Notification((MemberPlayer) player, description, context));
		}
	}

	protected void fireNotification(Notification notification) {
		final String name = notification.getDescription().getName();
		final boolean enabled = notificationManager.isNotificationEnabled(name, notification.getMember());

		if (log.isInfoEnabled()) {
			log.info("Fire notification " + name + " to person " + notification.getMember());
		}
		for (NotificationPublisher publisher : publishers) {
			publisher.publishNotification(notification, enabled);
		}
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

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public void setPublishers(Collection<NotificationPublisher> publishers) {
		this.publishers.clear();

		if (publishers != null) {
			this.publishers.addAll(publishers);
		}
	}

	public void setNotificationManager(NotificationManager notificationManager) {
		this.notificationManager = notificationManager;
	}

	public void setExpirationManager(GameExpirationManager expirationManager) {
		if (this.expirationManager != null) {
			this.expirationManager.removeGameExpirationListener(notificationListener);
		}
		this.expirationManager = expirationManager;
		if (this.expirationManager != null) {
			this.expirationManager.addGameExpirationListener(notificationListener);
		}
	}

	private class TheNotificationListener implements BoardStateListener, GameExpirationListener, MessageListener, GameProposalListener {
		private TheNotificationListener() {
		}

		@Override
		public void gameProposalInitiated(GameProposal proposal) {
			if (proposal instanceof ChallengeGameProposal) {
				final ChallengeGameProposal challenge = (ChallengeGameProposal) proposal;
				final NotificationDescription description = notificationManager.getDescription("game.challenge");
				if (description != null) {
					@SuppressWarnings("unchecked")
					final Collection<Personality> waitingPlayers = challenge.getWaitingPlayers();
					for (Personality player : waitingPlayers) {
						processNotification(player, description, challenge);
					}
				}
			}
		}

		@Override
		public void gameProposalUpdated(GameProposal proposal) {
		}

		@Override
		public void gameProposalClosed(GameProposal proposal) {
		}

		@Override
		public void gameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board) {
			final NotificationDescription description = notificationManager.getDescription("game.started");
			if (description != null) {
				final Collection<? extends GamePlayerHand> playersHands = board.getPlayersHands();
				for (GamePlayerHand hand : playersHands) {
					processNotification(hand.getPlayerId(), description, board);
				}
			}
		}

		@Override
		public void gameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move) {
			final NotificationDescription d1 = notificationManager.getDescription("game.move.your");
			final NotificationDescription d2 = notificationManager.getDescription("game.move.opponent");
			final Collection<? extends GamePlayerHand> playersHands = board.getPlayersHands();
			for (GamePlayerHand hand : playersHands) {
				if (board.getPlayerTurn() != null && board.getPlayerTurn().equals(hand)) {
					if (d1 != null) {
						processNotification(Personality.person(hand.getPlayerId()), d1, board);
					}
				} else {
					if (d2 != null && board.getPlayersHands().size() > 2) {
						processNotification(Personality.person(hand.getPlayerId()), d2, board);
					}
				}
			}
		}

		@Override
		public void gameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameResolution resolution, Collection<? extends GamePlayerHand> wonPlayers) {
			final NotificationDescription description = notificationManager.getDescription("game.finished");
			if (description != null) {
				final Collection<? extends GamePlayerHand> playersHands = board.getPlayersHands();
				for (GamePlayerHand hand : playersHands) {
					processNotification(Personality.person(hand.getPlayerId()), description, board);
				}
			}
		}

		@Override
		public void gameExpiring(long boardId, GameExpirationType expiration) {
			try {
				final GameBoard board = boardManager.openBoard(boardId);
				if (board != null) {
					final GamePlayerHand hand = board.getPlayerTurn();
					if (hand != null) {
						NotificationDescription description = notificationManager.getDescription(expiration.getCode());
						if (description != null) {
							processNotification(Personality.person(hand.getPlayerId()), description, board);
						}
					}
				}
			} catch (BoardLoadingException ignored) {
			}
		}

		@Override
		public void messageSent(Message message, boolean quite) {
			final NotificationDescription description = notificationManager.getDescription("game.message");
			if (description != null && !quite) {
				processNotification(Personality.person(message.getRecipient()), description, message);
			}
		}
	}
}
