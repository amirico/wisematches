package wisematches.server.deprecated.web.modules.app.events.producers;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GameBoardEventProducer {//implements EventProducer {
/*
    private PlayerManager playerManager;
    private EventNotificator notificator;
    private RoomManagerFacade roomManagerFacade;

    private final GameBoardListener gameBoardListener = new GameBoardListener();

    private static final Log log = LogFactory.getLog(GameBoardEventProducer.class);

    public void activateProducer(EventNotificator notificator) {
        if (log.isDebugEnabled()) {
            log.debug("Activate events producer with notificator: " + notificator);
        }
        this.notificator = notificator;
    }

    public void deactivateProducer() {
        if (log.isDebugEnabled()) {
            log.debug("Deactivate events producer");
        }
        notificator = null;
    }

    */
/**
 * This method converts specified {@code ScribbleBoard} to {@code DashboardItemBean}.
 *
 * @param board		 the board to convert
 * @param playerManager the player manager is used to load information about players.
 * @return the converted {@code DashboardItemBean}
 *//*

    public static DashboardItemBean convertDashboard(ScribbleBoard board, PlayerManager playerManager) {
        final ScribbleSettings settings = board.getGameSettings();
        final List<ScribblePlayerHand> playersHands = board.getPlayersHands();

        final PlayerInfoBean[] players = new PlayerInfoBean[settings.getMaxPlayers()];
        for (int i = 0; i < playersHands.size(); i++) {
            final ScribblePlayerHand hand = playersHands.get(i);
            players[i] = convertPlayer(hand.getPlayerId(), playerManager);
            players[i].setCurrentRating(0);
        }

        final DashboardItemBean item = new DashboardItemBean(board.getBoardId(), settings.getTitle(), players,
                settings.getLanguage(), settings.getDaysPerMove());
        item.setMinRating(settings.getMinRating());
        item.setMaxRating(settings.getMaxRating());
        return item;
    }

    public static PlayerMoveBean convertGameMove(GameMove gameMove) {
        final PlayerMove playerMove = gameMove.getPlayerMove();
        if (playerMove instanceof MakeWordMove) {
            final MakeWordMove m = (MakeWordMove) playerMove;
            return new PlayerMoveBean(playerMove.getPlayerId(), gameMove.getMoveNumber(), gameMove.getMoveTime(), m.getWord(), gameMove.getPoints());
        } else if (playerMove instanceof ExchangeTilesMove) {
            return new PlayerMoveBean(playerMove.getPlayerId(), gameMove.getMoveNumber(), gameMove.getMoveTime(), PlayerMoveBean.Type.EXCHANGE, gameMove.getPoints());
        } else if (playerMove instanceof PassTurnMove) {
            return new PlayerMoveBean(playerMove.getPlayerId(), gameMove.getMoveNumber(), gameMove.getMoveTime(), PlayerMoveBean.Type.PASSED, gameMove.getPoints());
        }
        return null;
    }


    public void setRoomManagerFacade(RoomManagerFacade roomManagerFacade) {
        if (this.roomManagerFacade != null) {
            this.roomManagerFacade.removeGameStateListener(gameBoardListener);
            this.roomManagerFacade.removeGameMoveListener(gameBoardListener);
            this.roomManagerFacade.removeGamePlayersListener(gameBoardListener);
        }

        this.roomManagerFacade = roomManagerFacade;

        if (this.roomManagerFacade != null) {
            this.roomManagerFacade.addGameStateListener(gameBoardListener);
            this.roomManagerFacade.addGameMoveListener(gameBoardListener);
            this.roomManagerFacade.addGamePlayersListener(gameBoardListener);
        }
    }

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }


    private class GameBoardListener implements GameMoveListener, GameStateListener, GamePlayersListener {
        public void playerAdded(GameBoard board, Player player) {
            if (notificator == null) {
                return;
            }

            final List playersHands = board.getPlayersHands();
            if (playersHands.size() == 1) {
                final DashboardItemBean bean = convertDashboard((ScribbleBoard) board, playerManager);
                notificator.fireEvent(new GameCreatedEvent(bean));
            } else {
                notificator.fireEvent(
                        new GamePlayersEvent(board.getBoardId(), convertPlayer(player.getId(), playerManager), GamePlayersEvent.Action.ADDED)
                );
            }
        }

        public void playerRemoved(GameBoard board, Player player) {
            if (notificator == null) {
                return;
            }

            notificator.fireEvent(new GamePlayersEvent(board.getBoardId(), convertPlayer(player), GamePlayersEvent.Action.REMOVED));
        }

        public void gameStarted(GameBoard board, GamePlayerHand playerTurn) {
            if (notificator == null) {
                return;
            }

            @SuppressWarnings("unchecked")
            final List<ScribblePlayerHand> hands = board.getPlayersHands();

            final long boardId = board.getBoardId();
            final long startedTime = board.getStartedTime();
            final long playerTrun = playerTurn.getPlayerId();

            for (ScribblePlayerHand hand : hands) {
                notificator.fireEvent(
                        new GameStartedEvent(boardId, playerTrun, startedTime, hand.getPlayerId(), hand.getTiles())
                );
            }
            notificator.fireEvent(new GameStartedEvent(boardId, playerTrun, startedTime));
        }

        public void playerMoved(GameMoveEvent event) {
            if (notificator == null) {
                return;
            }

            final ScribbleBoard board = (ScribbleBoard) event.getGameBoard();
            final ScribblePlayerHand hand = (ScribblePlayerHand) event.getPlayer();
            final ScribblePlayerHand nextPlayerHand = (ScribblePlayerHand) event.getNextPlayer();

            final PlayerMoveBean move = convertGameMove(event.getGameMove());
            if (move != null) {
                if (nextPlayerHand != null) {
                    notificator.fireEvent(new GameTurnEvent(board.getBoardId(), nextPlayerHand.getPlayerId(), hand.getTiles().length, move));
                } else {
                    notificator.fireEvent(new GameTurnEvent(board.getBoardId(), hand.getTiles().length, move));
                }
            }
        }

        public void gameFinished(GameBoard board, GamePlayerHand wonPlayer) {
            fireGameFinishedEvent(board, GameFinishedEvent.Type.FINISHED, wonPlayer);
        }

        public void gameDraw(GameBoard board) {
            fireGameFinishedEvent(board, GameFinishedEvent.Type.FINISHED, null);
        }

        public void gameInterrupted(GameBoard board, GamePlayerHand interrupterPlayer, boolean byTimeout) {
            if (byTimeout) {
                fireGameFinishedEvent(board, GameFinishedEvent.Type.TIMEDOUT, interrupterPlayer);
            } else {
                fireGameFinishedEvent(board, GameFinishedEvent.Type.RESIGNED, interrupterPlayer);
            }
        }

        private void fireGameFinishedEvent(GameBoard board, GameFinishedEvent.Type type, GamePlayerHand player) {
            if (notificator == null) {
                return;
            }
            @SuppressWarnings("unchecked")
            final List<GamePlayerHand> hands = board.getPlayersHands();
            final GameFinishedEvent.FinalPoint[] points = new GameFinishedEvent.FinalPoint[hands.size()];
            for (int i = 0, handsSize = hands.size(); i < handsSize; i++) {
                final GamePlayerHand hand = hands.get(i);
                points[i] = new GameFinishedEvent.FinalPoint(hand.getPlayerId(), hand.getPoints());
            }

            notificator.fireEvent(new GameFinishedEvent(board.getBoardId(), type,
                    board.isRatedGame(), player == null ? 0 : player.getPlayerId(), board.getFinishedTime(), points));
        }
    }
*/
}
