package wisematches.server.deprecated.web.modules.app.notification;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import wisematches.server.deprecated.web.mail.FromTeam;
import wisematches.server.deprecated.web.mail.MailSender;
import wisematches.server.gameplaying.board.*;
import wisematches.server.gameplaying.cleaner.GameTimeoutEvent;
import wisematches.server.gameplaying.cleaner.GameTimeoutListener;
import wisematches.server.gameplaying.cleaner.GameTimeoutTerminator;
import wisematches.server.gameplaying.room.*;
import wisematches.server.player.Player;
import wisematches.server.player.PlayerManager;
import wisematches.server.standing.notice.PlayerNotification;
import wisematches.server.utils.sessions.PlayerSessionsManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO: extract abstract notifications provider
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class EMailNotificationsSender {
	private MailSender mailSender;
	private PlayerManager playerManager;
	private RoomsManager roomsManager;

	private TransactionTemplate transactionTemplate;
	private GameTimeoutTerminator gameTimeoutProcessor;
	private PlayerSessionsManager playerSessionsManager;

	private final RoomBoardsListener roomBoardsListener = new TheRoomBoardsListener();
	private final GameTimeoutListener gameTimeoutListener = new TheGameTimeoutListener();
	private final ExecutorService executor = Executors.newSingleThreadExecutor(new CustomizableThreadFactory("NotificationsSender"));

	private static final Log log = LogFactory.getLog(EMailNotificationsSender.class);

	public EMailNotificationsSender() {
	}

	private void sentNotification(PlayerNotification notificationType,
								  String resource,
								  List<GamePlayerHand> players, Map<String, ?> model) {
		for (GamePlayerHand hand : players) {
			sentNotification(notificationType, resource, hand, model);
		}
	}

	private void sentNotification(final PlayerNotification notificationType, final String resource,
								  final GamePlayerHand hand, final Map<String, ?> model) {

		class NotificationWorkUnit implements Runnable, TransactionCallback {
			@Override
			public void run() {
				transactionTemplate.execute(this); // call doInTransaction
			}

			@Override
			public Object doInTransaction(TransactionStatus status) {
				try {
					final long playerId = hand.getPlayerId();
					if (log.isDebugEnabled()) {
						log.debug("Send notification " + notificationType + " to " + playerId + " with resource " + resource);
					}

					final Player p = playerManager.getPlayer(playerId);
					final boolean playerOnline = playerSessionsManager.isPlayerOnline(p);

					if (!playerOnline || notificationType.isOnlineNotification()) {
						if (isPlayerAllowsNotification(p, notificationType)) {
							mailSender.sendMail(FromTeam.ABSTRACT, p, resource, model);
						} else {
							if (log.isDebugEnabled()) {
								log.debug("Player disabled notifications of these type: notification ignored.");
							}
						}
					} else {
						if (log.isDebugEnabled()) {
							log.debug("Player is online: notification ignored");
						}
					}
				} catch (Exception ex) {
					log.error("Notification can't be sent to player " + hand, ex);
				}
				return null;
			}
		}
		executor.execute(new NotificationWorkUnit());
	}

	protected boolean isPlayerAllowsNotification(Player p, PlayerNotification notification) {
		throw new UnsupportedOperationException("Commented");
//		return p.getPlayerNotifications().isNotificationEnabled(notification);
	}

	private void listenBoardEvents(GameBoard board) {
		final TheBoardListener l = new TheBoardListener();
		board.addGameStateListener(l);
		board.addGameMoveListener(l);
		board.addGamePlayersListener(l);
	}

	public void destroy() {
		executor.shutdown();
	}

	public void setRoomsManager(RoomsManager roomsManager) {
		if (this.roomsManager != null) {
			final Collection<RoomManager> roomManagerCollection = this.roomsManager.getRoomManagers();
			for (RoomManager roomManager : roomManagerCollection) {
				roomManager.removeRoomBoardsListener(roomBoardsListener);
			}
		}

		this.roomsManager = roomsManager;

		if (roomsManager != null) {
			final Collection<RoomManager> roomManagerCollection = roomsManager.getRoomManagers();
			for (RoomManager roomManager : roomManagerCollection) {
				@SuppressWarnings("unchecked")
				final Collection<GameBoard> collection = roomManager.getOpenedBoards();
				for (GameBoard board : collection) {
					listenBoardEvents(board);
				}
				roomManager.addRoomBoardsListener(roomBoardsListener);
			}
		}
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public void setGameTimeoutProcessor(GameTimeoutTerminator gameTimeoutProcessor) {
		if (this.gameTimeoutProcessor != null) {
			this.gameTimeoutProcessor.removeGameTimeoutListener(gameTimeoutListener);
		}

		this.gameTimeoutProcessor = gameTimeoutProcessor;

		if (this.gameTimeoutProcessor != null) {
			this.gameTimeoutProcessor.addGameTimeoutListener(gameTimeoutListener);
		}
	}

	public void setPlayerSessionsManager(PlayerSessionsManager playerSessionsManager) {
		this.playerSessionsManager = playerSessionsManager;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	private class TheRoomBoardsListener implements RoomBoardsListener {
		public void boardOpened(Room room, long boardId) {
			try {
				final GameBoard board = roomsManager.getRoomManager(room).openBoard(boardId);

				listenBoardEvents(board);
			} catch (BoardLoadingException ex) {
				log.error("Error opening game in boardOpened event processor", ex);
			}

		}

		public void boardClosed(Room room, long boardId) {
		}
	}

	private class TheBoardListener implements GameStateListener, GameMoveListener, GamePlayersListener {
		public void gameStarted(GameBoard board, GamePlayerHand playerTurn) {
			@SuppressWarnings("unchecked")
			final List<GamePlayerHand> playersHands = board.getPlayersHands();
			final Player turnOwnerPlayer = playerManager.getPlayer(playerTurn.getPlayerId());

			final Map<String, Object> model = new HashMap<String, Object>();
			model.put("board", board);
			model.put("turnOwnerPlayer", turnOwnerPlayer);
			model.put("timeoutTime", System.currentTimeMillis() - board.getLastMoveTime());

			for (GamePlayerHand hand : playersHands) {
				if (hand == playerTurn) {
					continue;
				}
				sentNotification(GameBoardNotification.GAME_STARTED, "app.game.started.other", hand, model);
			}
			sentNotification(GameBoardNotification.GAME_STARTED, "app.game.started.you", playerTurn, model);
		}

		public void gameFinished(GameBoard board, GamePlayerHand wonPlayer) {
			final Map<String, Object> model = new HashMap<String, Object>();
			model.put("board", board);
			model.put("wonPlayer", playerManager.getPlayer(wonPlayer.getPlayerId()));
			model.put("rated", board.isRatedGame());

			@SuppressWarnings("unchecked")
			final List<GamePlayerHand> hands = board.getPlayersHands();
			for (GamePlayerHand hand : hands) {
				if (hand == wonPlayer) {
					continue;
				}
				sentNotification(GameBoardNotification.GAME_FINISHED, "app.game.finished.lost", hand, model);
			}
			sentNotification(GameBoardNotification.GAME_FINISHED, "app.game.finished.won", wonPlayer, model);
		}

		@SuppressWarnings("unchecked")
		public void gameDraw(GameBoard board) {
			final Map<String, Object> model = new HashMap<String, Object>();
			model.put("board", board);
			model.put("rated", board.isRatedGame());

			sentNotification(GameBoardNotification.GAME_FINISHED, "app.game.finished.draw", board.getPlayersHands(), model);
		}

		@SuppressWarnings("unchecked")
		public void gameInterrupted(GameBoard board, GamePlayerHand interrupterPlayer, boolean byTimeout) {
			final Map<String, Object> model = new HashMap<String, Object>();
			model.put("board", board);
			model.put("timeouted", byTimeout);
			model.put("rated", board.isRatedGame());
			model.put("interrupterPlayer", playerManager.getPlayer(interrupterPlayer.getPlayerId()));

			sentNotification(GameBoardNotification.GAME_FINISHED, "app.game.finished.lost", interrupterPlayer, model);

			@SuppressWarnings("unchecked")
			final List<GamePlayerHand> hands = board.getPlayersHands();
			final GamePlayerHand wonPlayer = board.getWonPlayer();
			if (wonPlayer != null) {
				model.put("wonPlayer", playerManager.getPlayer(wonPlayer.getPlayerId()));
				sentNotification(GameBoardNotification.GAME_FINISHED, "app.game.finished.won", wonPlayer, model);
				for (GamePlayerHand hand : hands) {
					if (hand == wonPlayer || hand == interrupterPlayer) {
						continue;
					}
					sentNotification(GameBoardNotification.GAME_FINISHED, "app.game.finished.lost", hand, model);
				}
			} else {
				for (GamePlayerHand hand : hands) {
					if (hand == interrupterPlayer) {
						continue;
					}
					sentNotification(GameBoardNotification.GAME_FINISHED, "app.game.finished.draw", board.getPlayersHands(), model);
				}
			}
		}

		public void playerMoved(GameMoveEvent event) {
			final GameBoard board = event.getGameBoard();
			final GamePlayerHand nextPlayer = event.getNextPlayer();

			final Map<String, Object> model = new HashMap<String, Object>();
			model.put("board", board);
			model.put("move", event.getGameMove().getPlayerMove());
			model.put("points", event.getGameMove().getPoints());
			model.put("movedPlayer", playerManager.getPlayer(event.getPlayer().getPlayerId()));

			if (nextPlayer != null) {
				model.put("nextPlayer", playerManager.getPlayer(nextPlayer.getPlayerId()));
			}

			if (nextPlayer != null) {
				sentNotification(GameBoardNotification.PLAYER_MOVED, "app.game.turn.you", nextPlayer, model);
			}
			@SuppressWarnings("unchecked")
			final List<GamePlayerHand> hands = board.getPlayersHands();
			for (GamePlayerHand hand : hands) {
				if (hand == nextPlayer || hand == event.getPlayer()) {
					continue;
				}
				sentNotification(GameBoardNotification.PLAYER_MOVED, "app.game.turn.other", hand, model);
			}
		}

		public void playerAdded(GameBoard board, Player player) {
			if (board.getGameSettings().getMaxPlayers() == board.getPlayersHands().size()) {
				return;
			}
			playerChanged(board, player, true, GameBoardNotification.PLAYER_ADDED);
		}

		public void playerRemoved(GameBoard board, Player player) {
			playerChanged(board, player, false, GameBoardNotification.PLAYER_REMOVED);
		}

		private void playerChanged(GameBoard board, Player player, final boolean joined, final GameBoardNotification type) {
			final Map<String, Object> model = new HashMap<String, Object>();
			model.put("board", board);
			model.put("changedPlayer", player);
			model.put("joined", joined);

			@SuppressWarnings("unchecked")
			final List<GamePlayerHand> hands = board.getPlayersHands();
			for (GamePlayerHand hand : hands) {
				if (hand != null && hand.getPlayerId() != player.getId()) {
					if (joined) {
						sentNotification(type, "app.game.player.joined", hand, model);
					} else {
						sentNotification(type, "app.game.player.left", hand, model);
					}
				}
			}
		}
	}

	private class TheGameTimeoutListener implements GameTimeoutListener {
		public void timeIsRunningOut(GameTimeoutEvent event) {
			final Map<String, Object> model = new HashMap<String, Object>();

			try {
				GameBoard board = roomsManager.getRoomManager(event.getRoom()).openBoard(event.getBoardId());
				model.put("board", board);
				model.put("remainderTime", event.getRemainderType());

				final GamePlayerHand turn = board.getPlayerTrun();
				if (turn != null) {
					sentNotification(GameBoardNotification.TIME_IS_RUNNING, "app.game.time.running", turn, model);
				}
			} catch (BoardLoadingException ex) {
				log.error("Timeout notifications can't be sent because board can't be loaded", ex);
			}
		}

		public void timeIsUp(GameTimeoutEvent event) {
		}
	}
}
