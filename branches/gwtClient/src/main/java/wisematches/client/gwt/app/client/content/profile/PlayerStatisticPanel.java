package wisematches.client.gwt.app.client.content.profile;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerStatisticPanel {//extends WMInfoWidget {
//    private final long currentPlayer;
//    private final PlayerProfileServiceAsync playerStatisticService;
//
//    public PlayerStatisticPanel(long currentPlayer, EventsDispatcher eventsDispatcher, PlayerProfileServiceAsync playerStatisticService) {
//        super(APP.ttlYourProgress());
//
//        this.currentPlayer = currentPlayer;
//        this.playerStatisticService = playerStatisticService;
//
//        initPanel();
//
//        refresh();
///*
//
//        final TheEventsListener eventsListener = new TheEventsListener();
//        eventsDispatcher.addEventsListener(eventsListener, eventsListener);
//*/
//    }
//
//    private void initPanel() {
//        setCollapsible(false);
//
//        addInfoItem("currentRating", APP.lblCurrentRating(), APP.lblNA());
//        addInfoItem("currentPosition", APP.lblCurrentPosition(), APP.lblNA());
//        addSeparator();
//        addInfoItem("activeGames", APP.lblActiveGames(), APP.lblNA());
//        addInfoItem("finishedGames", APP.lblFinishedGames(), APP.lblNA());
//        addSeparator();
//        addInfoItem("wonGames", APP.lblWonGames(), APP.lblNA());
//        addInfoItem("lostGames", APP.lblLostGames(), APP.lblNA());
//        addInfoItem("drawnGames", APP.lblDrawnGames(), APP.lblNA());
//    }
//
//    private void updatePlayerGameStatistic(PlayerStatisticBean s) {
//        setInfoValue("currentRating", s.getCurrentRating());
//        setInfoValue("currentPosition", s.getCurrentPosition());
//        setInfoValue("activeGames", s.getActiveGames());
//        setInfoValue("finishedGames", s.getFinishedGames());
//        setInfoValue("wonGames", s.getWonGames());
//        setInfoValue("lostGames", s.getLostGames());
//        setInfoValue("drawnGames", s.getDrawGames());
//    }
//
//    public void refresh() {
//        playerStatisticService.getPlayerStatisticBean(currentPlayer, new AsyncCallback<PlayerStatisticBean>() {
//            public void onFailure(Throwable throwable) {
//                ExceptionHandler.showSystemError(throwable);
//            }
//
//            public void onSuccess(PlayerStatisticBean s) {
//                updatePlayerGameStatistic(s);
//            }
//        });
//    }
///*
//
//    private class TheEventsListener implements EventsListener<PlayerStatisticEvent>, Correlator<PlayerStatisticEvent> {
//        public void eventsReceived(Collection<PlayerStatisticEvent> evens) {
//            for (PlayerStatisticEvent even : evens) {
//                if (even.getPlayerId() == currentPlayer) {
//                    refresh();
//                }
//            }
//        }
//
//        public boolean isEventSupported(Event e) {
//            return (e instanceof PlayerStatisticEvent);
//        }
//
//        public Correlation eventsColleration(PlayerStatisticEvent e1, PlayerStatisticEvent e2) {
//            return e1.eventCorrelation(e2);
//        }
//    }
//*/
}
