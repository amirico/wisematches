package wisematches.server.deprecated.web.modules.app.notification;


/**
 * TODO: extract abstract notifications provider
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class EMailNotificationsSender {
/*
	private MailSender mailSender;
	private PlayerManager playerManager;
	private RoomsManager roomsManager;

	private TransactionTemplate transactionTemplate;
	private GameTimeoutTerminator gameTimeoutProcessor;
	private PlayerSessionsManager playerSessionsManager;

	private final BoardStateListener boardListener = new TheBoardStateListener();
	private final GameTimeoutListener gameTimeoutListener = new TheGameTimeoutListener();
	private final ExecutorService executor = Executors.newSingleThreadExecutor(new CustomizableThreadFactory("NotificationsSender"));

	private static final Log log = LogFactory.getLog(EMailNotificationsSender.class);

	public EMailNotificationsSender() {
	}

	private void sentNotification(PlayerNotification notificationType,
								  String resource,
								  Collection<GamePlayerHand> players, Map<String, ?> model) {
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

	public void destroy() {
		executor.shutdown();
	}

	public void setRoomsManager(RoomsManager roomsManager) {
		if (this.roomsManager != null) {
			final Collection<RoomManager> roomManagerCollection = this.roomsManager.getRoomManagers();
			for (RoomManager roomManager : roomManagerCollection) {
				roomManager.getBoardManager().removeBoardStateListener(boardListener);
			}
		}

		this.roomsManager = roomsManager;

		if (roomsManager != null) {
			final Collection<RoomManager> roomManagerCollection = roomsManager.getRoomManagers();
			for (RoomManager roomManager : roomManagerCollection) {
				roomManager.getBoardManager().addBoardStateListener(boardListener);
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

	private class TheBoardStateListener implements BoardStateListener {
		@Override
		public void gameStarted(GameBoard board) {
			@SuppressWarnings("unchecked")
			final Collection<GamePlayerHand> playersHands = board.getPlayersHands();
			final Player turnOwnerPlayer = playerManager.getPlayer(board.getPlayerTurn().getPlayerId());

			final Map<String, Object> model = new HashMap<String, Object>();
			model.put("board", board);
			model.put("turnOwnerPlayer", turnOwnerPlayer);
			model.put("timeoutTime", System.currentTimeMillis() - board.getLastMoveTime().getTime());

			for (GamePlayerHand hand : playersHands) {
				if (hand == board.getPlayerTurn()) {
					continue;
				}
				sentNotification(GameBoardNotification.GAME_STARTED, "app.game.started.other", hand, model);
			}
			sentNotification(GameBoardNotification.GAME_STARTED, "app.game.started.you", board.getPlayerTurn(), model);
		}

		@Override
		public <S extends GameSettings, P extends GamePlayerHand> void gameFinished(GameBoard<S, P> board, GameResolution resolution, Collection<P> wonPlayers) {
			final Map<String, Object> model = new HashMap<String, Object>();
			model.put("board", board);
//			model.put("wonPlayer", playerManager.getPlayer(wonPlayer.getPlayerId()));
			model.put("rated", board.isRatedGame());

			@SuppressWarnings("unchecked")
			final Collection<P> hands = board.getPlayersHands();
			for (P hand : hands) {
				if (!wonPlayers.contains(hand)) {
					continue;
				}
				sentNotification(GameBoardNotification.GAME_FINISHED, "app.game.finished.lost", hand, model);
			}
//			sentNotification(GameBoardNotification.GAME_FINISHED, "app.game.finished.won", wonPlayer, model);
*/
/*
			final Map<String, Object> model = new HashMap<String, Object>();
			model.put("board", board);
			model.put("timeouted", byTimeout);
			model.put("rated", board.isRatedGame());
			model.put("interrupterPlayer", playerManager.getPlayer(interrupterPlayer.getPlayerId()));

			sentNotification(GameBoardNotification.GAME_FINISHED, "app.game.finished.lost", interrupterPlayer, model);

			@SuppressWarnings("unchecked")
			final List<P> hands = board.getPlayersHands();
			final Collection<P> wonPlayer = board.getWonPlayers();
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
*//*

		}

		@Override
		public void gameMoveDone(GameBoard board, GameMove move) {
			final GamePlayerHand nextPlayer = board.getPlayerTurn();

			final Map<String, Object> model = new HashMap<String, Object>();
			model.put("board", board);
			model.put("move", move);
			model.put("points", move.getPoints());
			model.put("movedPlayer", playerManager.getPlayer(move.getPlayerMove().getPlayerId()));

			if (nextPlayer != null) {
				model.put("nextPlayer", playerManager.getPlayer(nextPlayer.getPlayerId()));
			}

			if (nextPlayer != null) {
				sentNotification(GameBoardNotification.PLAYER_MOVED, "app.game.turn.you", nextPlayer, model);
			}
			@SuppressWarnings("unchecked")
			final Collection<GamePlayerHand> hands = board.getPlayersHands();
			for (GamePlayerHand hand : hands) {
				if (hand == nextPlayer || hand.getPlayerId() == move.getPlayerMove().getPlayerId()) {
					continue;
				}
				sentNotification(GameBoardNotification.PLAYER_MOVED, "app.game.turn.other", hand, model);
			}
		}
	}

	private class TheGameTimeoutListener implements GameTimeoutListener {
		public void timeIsRunningOut(GameTimeoutEvent event) {
			final Map<String, Object> model = new HashMap<String, Object>();

			try {
				final GameBoard board = roomsManager.getBoardManager(event.getRoom()).openBoard(event.getBoardId());
				model.put("board", board);
				model.put("remainderTime", event.getRemainderType());

				final GamePlayerHand turn = board.getPlayerTurn();
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
*/
}
