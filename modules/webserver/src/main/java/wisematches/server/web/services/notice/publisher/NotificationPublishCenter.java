package wisematches.server.web.services.notice.publisher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.personality.Personality;
import wisematches.personality.player.computer.ComputerPlayer;
import wisematches.playground.*;
import wisematches.playground.expiration.GameExpirationListener;
import wisematches.playground.expiration.GameExpirationManager;
import wisematches.playground.expiration.GameExpirationType;
import wisematches.playground.message.Message;
import wisematches.playground.message.MessageListener;
import wisematches.playground.message.MessageManager;
import wisematches.server.web.services.notice.NotificationDescription;
import wisematches.server.web.services.notice.NotificationManager;
import wisematches.server.web.services.state.PlayerStateListener;
import wisematches.server.web.services.state.PlayerStateManager;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class NotificationPublishCenter {
	private BoardManager boardManager;
	private MessageManager messageManager;
	private PlayerStateManager playerStateManager;
	private GameExpirationManager expirationManager;
	private NotificationManager notificationManager;

	private final Lock lock = new ReentrantLock();
	private final Map<Personality, List<Notification>> bufferedNotification = new HashMap<Personality, List<Notification>>();

	private final PlayerStateListener stateListener = new ThePlayerStateListener();
	private final TheNotificationListener notificationListener = new TheNotificationListener();
	private final Collection<NotificationPublisher> publishers = new ArrayList<NotificationPublisher>();

	private static final Log log = LogFactory.getLog("wisematches.server.notice.publisher");

	public NotificationPublishCenter() {
	}

	protected void processNotification(Notification notification) {
		if (log.isDebugEnabled()) {
			log.debug("Process notification " + notification);
		}

		final NotificationDescription description = notification.getDescription();
		final Personality personality = notification.getPersonality();
		if (personality instanceof ComputerPlayer) {
			return;
		}

		if (notificationManager.isNotificationEnabled(description.getName(), personality)) {
			if (description.isEvenOnline()) {
				fireNotification(notification);
			} else {
				if (!playerStateManager.isPlayerOnline(personality)) {
					fireNotification(notification);
				} else {
					if (log.isDebugEnabled()) {
						log.debug("Player if online, put notification into queue");
					}
					lock.lock();
					try {
						List<Notification> notifications = bufferedNotification.get(personality);
						if (notifications == null) {
							notifications = new ArrayList<Notification>();
							bufferedNotification.put(personality, notifications);
						}
						notifications.add(notification);
					} finally {
						lock.unlock();
					}
				}
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Notification is disabled for player " + personality);
			}
		}
	}

	protected void processNotifications(List<Notification> notifications) {
		final Set<String> series = new HashSet<String>();

		Collections.reverse(notifications);
		for (Notification n : notifications) {
			final NotificationDescription d = n.getDescription();
			final String s = d.getSeries();
			if (s != null && !s.isEmpty()) {
				if (series.contains(s)) {
					continue;
				}
				series.add(s);
			}
			processNotification(n);
		}
	}


	protected void fireNotification(Notification notification) {
		if (log.isInfoEnabled()) {
			log.info("Fire notification " + notification.getDescription().getName() + " to person " + notification.getPersonality());
		}

		for (NotificationPublisher publisher : publishers) {
			publisher.publishNotification(notification);
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

	public void setPublishers(Collection<NotificationPublisher> publishers) {
		this.publishers.clear();

		if (publishers != null) {
			this.publishers.addAll(publishers);
		}
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

	public void setPlayerStateManager(PlayerStateManager playerStateManager) {
		if (this.playerStateManager != null) {
			this.playerStateManager.removePlayerStateListener(stateListener);
		}

		this.playerStateManager = playerStateManager;

		if (this.playerStateManager != null) {
			this.playerStateManager.addPlayerStateListener(stateListener);
		}
	}

	public void setMessageManager(MessageManager messageManager) {
		if (this.messageManager != null) {
			this.messageManager.removeMessageListener(notificationListener);
		}
		this.messageManager = messageManager;
		if (this.messageManager != null) {
			this.messageManager.addMessageListener(notificationListener);
		}
	}

	public void setNotificationManager(NotificationManager notificationManager) {
		this.notificationManager = notificationManager;
	}

	private class ThePlayerStateListener implements PlayerStateListener {
		private ThePlayerStateListener() {
		}

		@Override
		public void playerOnline(Personality person) {
		}

		@Override
		public void playerAlive(Personality person) {
			lock.lock();
			try {
				bufferedNotification.remove(person);
			} finally {
				lock.unlock();
			}
		}

		@Override
		public void playerOffline(Personality person) {
			final List<Notification> remove;
			lock.lock();
			try {
				remove = bufferedNotification.remove(person);
			} finally {
				lock.unlock();
			}
			if (remove != null) {
				processNotifications(remove);
			}
		}
	}

	private class TheNotificationListener implements BoardStateListener, GameExpirationListener, MessageListener {
		private TheNotificationListener() {
		}

		@Override
		public void gameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board) {
			final NotificationDescription description = notificationManager.getDescription("game.started");
			if (description != null) {
				final Collection<? extends GamePlayerHand> playersHands = board.getPlayersHands();
				for (GamePlayerHand hand : playersHands) {
					processNotification(new Notification(Personality.person(hand.getPlayerId()), description, board));
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
						processNotification(new Notification(Personality.person(hand.getPlayerId()), d1, board));
					}
				} else {
					if (d2 != null && board.getPlayersHands().size() > 2) {
						processNotification(new Notification(Personality.person(hand.getPlayerId()), d2, board));
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
					processNotification(new Notification(Personality.person(hand.getPlayerId()), description, board));
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
							processNotification(new Notification(Personality.person(hand.getPlayerId()), description, board));
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
				processNotification(new Notification(Personality.person(message.getRecipient()), description, message));
			}
		}
	}
}
