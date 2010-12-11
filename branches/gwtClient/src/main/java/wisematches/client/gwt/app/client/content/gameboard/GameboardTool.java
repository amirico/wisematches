package wisematches.client.gwt.app.client.content.gameboard;

import wisematches.client.gwt.app.client.ApplicationFrame;
import wisematches.client.gwt.app.client.ApplicationTool;
import wisematches.client.gwt.app.client.ToolReadyCallback;

/**
 * This tool contains information about all active games for current player.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GameboardTool implements ApplicationTool {
//    private final Store activeGames = new Store(RECORD_DEF);
//
//    private PlayerSessionTool playerSessionTool;
//
//    private static final GameboardServiceAsync gameboardService = GameboardService.App.getInstance();
//
//    private static final FieldDef[] RECORD_DEFS = new FieldDef[]{
//            new StringFieldDef("boardid"),
//            new StringFieldDef("title"),
//            new ObjectFieldDef("players"),
//            new StringFieldDef("locale"),
//            new ObjectFieldDef("playermove"),
//            new DateFieldDef("lastMoveTime"),
//            new ObjectFieldDef("item"),
//    };
//    private static final RecordDef RECORD_DEF = new RecordDef(RECORD_DEFS);
//
//    private static GameboardTool instance;
//
//    public GameboardTool() {
//        if (instance != null) {
//            throw new NullPointerException("This tool already exist");
//        }
//        instance = this;
//    }
//
//    public native void registerJSCallbacks() /*-{
//    }-*/;
//
//    public void initializeTool(final ApplicationFrame applicationFrame, final ToolReadyCallback callback) {
//        playerSessionTool = applicationFrame.getApplicationTool(PlayerSessionTool.class);
//
//        // init listeners
//        final EventsDispatcher eventsDispatcher = applicationFrame.getApplicationTool(EventsDispatcherTool.class).getEventsDispatcher();
//        final TheGamesEventsListener eventsListener = new TheGamesEventsListener();
//        eventsDispatcher.addEventsListener(eventsListener, eventsListener);
//
//        // load active games
//        gameboardService.loadPlayerGames(playerSessionTool.getCurrentPlayer(), new AsyncCallback<GameboardItemBean[]>() {
//            public void onFailure(Throwable throwable) {
//                ExceptionHandler.showSystemError(throwable);
//            }
//
//            public void onSuccess(GameboardItemBean[] gameboardItemBeans) {
//                for (GameboardItemBean bean : gameboardItemBeans) {
//                    activeGames.add(convertGameboardItem(bean));
//                }
//                callback.toolReady(GameboardTool.this);
//            }
//        });
//    }
//
//    public GameboardTableView createActiveGamesView(Settings settings, String createNewGameLink) {
//        return new GameboardTable(getActiveGames(), settings, playerSessionTool.getCurrentPlayer(), createNewGameLink);
//    }
//
//    public GameboardItemBean getActiveBoard(long boardId) {
//        final Record id = activeGames.getById(String.valueOf(boardId));
//        if (id == null) {
//            return null;
//        }
//        return (GameboardItemBean) id.getAsObject("item");
//    }
//
//    protected Store getActiveGames() {
//        return activeGames;
//    }
//
//    private Record convertGameboardItem(final GameboardItemBean bean) {
//        return RECORD_DEF.createRecord(String.valueOf(bean.getBoardId()), new Object[]{
//                String.valueOf(bean.getBoardId()),
//                bean.getTitle(),
//                new JSArray<PlayerInfoBean>(bean.getPlayers()).getJavaScriptObject(),
//                Localization.convertLocale(bean.getLocale()),
//                bean.getPlayerInfoBean(bean.getPlayerMove()),
//                new Date(bean.getLastMoveTime()),
//                bean
//        });
//    }
//
//    private final class TheGamesEventsListener implements EventsListener<GameBoardEvent>, Correlator<GameBoardEvent> {
//        private final Map<Long, GameboardItemBean> createdGamesMap = new HashMap<Long, GameboardItemBean>();
//
//        public void eventsReceived(Collection<GameBoardEvent> evens) {
//            for (GameBoardEvent even : evens) {
//                final Record record = activeGames.getById(String.valueOf(even.getBoardId()));
//
//                if (record == null) {
//                    if (even instanceof GameCreatedEvent) {
//                        final GameCreatedEvent e = (GameCreatedEvent) even;
//                        final DashboardItemBean settings = e.getGameSettingsBean();
//
//                        final GameboardItemBean bean = new GameboardItemBean(); // Create new gameboard bean
//                        bean.setBoardId(settings.getBoardId());
//                        bean.setDaysPerMove(settings.getDaysPerMove());
//                        bean.setLocale(settings.getLocale());
//                        bean.setPlayers(settings.getPlayers());
//                        bean.setTitle(settings.getTitle());
//
//                        if (bean.getPlayerInfoBean(playerSessionTool.getCurrentPlayer()) == null) {
//                            createdGamesMap.put(e.getBoardId(), bean);
//                        } else {
//                            activeGames.add(convertGameboardItem(bean));
//                        }
//                    } else if (even instanceof GamePlayersEvent) {
//                        final GamePlayersEvent e = (GamePlayersEvent) even;
//
//                        final GameboardItemBean bean = createdGamesMap.get(e.getBoardId());
//                        if (bean != null) { // If game was created without this player
//                            if (e.getAction() == GamePlayersEvent.Action.ADDED) { //and new player added
//                                bean.addOpponent(e.getPlayerInfoBean());
//
//                                // and new player is this
//                                if (e.getPlayerInfoBean().getPlayerId() == playerSessionTool.getCurrentPlayer()) {
//                                    createdGamesMap.remove(e.getBoardId());
//                                    // when add this game to created.
//                                    activeGames.add(convertGameboardItem(bean));
//                                }
//                            } else if (e.getAction() == GamePlayersEvent.Action.REMOVED) {
//                                bean.removeOpponent(e.getPlayerInfoBean());
//                            }
//                        }
//                    } else if (even instanceof GameStartedEvent) { //If game started remove it from created games.
//                        final GameStartedEvent e = (GameStartedEvent) even;
//                        createdGamesMap.remove(e.getBoardId());
//                    }
//                } else {
//                    record.beginEdit();
//                    final GameboardItemBean bean = (GameboardItemBean) record.getAsObject("item");
//                    if (even instanceof GameStartedEvent) {
//                        final GameStartedEvent e = (GameStartedEvent) even;
//                        bean.setStartedTime(e.getStartTime());
//                        bean.setPlayerMove(e.getPlayerTrun());
//                        bean.setLastMoveTime(e.getStartTime());
//
//                        record.set("playermove", bean.getPlayerInfoBean(bean.getPlayerMove()));
//                        record.set("lastMoveTime", new Date(bean.getLastMoveTime()));
//                    } else if (even instanceof GamePlayersEvent) {
//                        final GamePlayersEvent e = (GamePlayersEvent) even;
//
//                        if (e.getAction() == GamePlayersEvent.Action.ADDED) {
//                            bean.addOpponent(e.getPlayerInfoBean());
//                            record.set("players", new JSArray<PlayerInfoBean>(bean.getPlayers()).getJavaScriptObject());
//                        } else if (e.getAction() == GamePlayersEvent.Action.REMOVED) {
//                            bean.removeOpponent(e.getPlayerInfoBean());
//
//                            if (e.getPlayerInfoBean().getPlayerId() == playerSessionTool.getCurrentPlayer()) {
//                                createdGamesMap.put(e.getBoardId(), bean);
//                                activeGames.remove(record);
//                            }
//                        }
//                    } else if (even instanceof GameTurnEvent) {
//                        final GameTurnEvent e = (GameTurnEvent) even;
//
//                        final PlayerMoveBean move = e.getPlayerMoveBean();
//
//                        final PlayerInfoBean movedPlayer = bean.getPlayerInfoBean(move.getPlayerId());
//                        if (movedPlayer != null) {
//                            movedPlayer.setCurrentRating(movedPlayer.getCurrentRating() + move.getPoints());
//                        }
//
//                        bean.setLastMoveTime(move.getMoveTime());
//                        bean.setPlayerMove(e.getNextPlayer());
//
//                        record.set("playermove", bean.getPlayerInfoBean(bean.getPlayerMove()));
//                        record.set("lastMoveTime", new Date(bean.getLastMoveTime()));
//                    } else if (even instanceof GameFinishedEvent) {
//                        final GameFinishedEvent e = (GameFinishedEvent) even;
//
//                        final GameFinishedEvent.FinalPoint[] finalPoints = e.getFinalPoints();
//                        for (GameFinishedEvent.FinalPoint point : finalPoints) {
//                            bean.getPlayerInfoBean(point.getPlayerId()).setCurrentRating(point.getPoints());
//                        }
//
//                        bean.setFinishedTime(e.getFinishedTime());
//                        bean.setPlayerMove(0);
//                        bean.setLastMoveTime(0);
//
//                        record.set("playermove", (PlayerInfoBean) null);
//                        record.set("lastMoveTime", (Date) null);
//
//                        activeGames.remove(record); // If game finished - remove it from active games.
//                    }
//                    if (activeGames.getById(record.getId()) != null) { //Still present...
//                        record.endEdit();
//                    }
//                }
//            }
//        }
//
//        public boolean isEventSupported(Event e) {
//            return e instanceof GameBoardEvent;
//        }
//
//        public Correlation eventsColleration(GameBoardEvent event1, GameBoardEvent event2) {
//            return Correlation.NEUTRAL;
//        }
//    }

	@Override
	public void registerJSCallbacks() {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void initializeTool(ApplicationFrame applicationFrame, ToolReadyCallback callback) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
