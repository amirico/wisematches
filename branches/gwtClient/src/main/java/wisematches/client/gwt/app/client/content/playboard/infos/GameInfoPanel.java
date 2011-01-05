package wisematches.client.gwt.app.client.content.playboard.infos;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GameInfoPanel { //extends WMInfoWidget {
/*
    private final long currentPlayer;
    private final ScribbleBoard scribbleBoard;
    private final PlayboardItemBean playboardItemBean;

    private static final DateTimeFormat DATE_TIME_FORMAT = DateTimeFormat.getShortDateTimeFormat();

    public GameInfoPanel(long currentPlayer, ScribbleBoard scribbleBoard, PlayboardItemBean playboardItemBean) {
        super(PB.ttlGameInfo());
        this.currentPlayer = currentPlayer;
        this.scribbleBoard = scribbleBoard;
        this.playboardItemBean = playboardItemBean;

        playboardItemBean.addPropertyChangeListener(new ThePropertyChangeListener());
        scribbleBoard.addPropertyChangeListener(new TheBoardPropertiesListener());

        initPanel();
        initData();
    }

    private void initPanel() {
        addInfoItem("started", PB.lblGameInfoStarted(), APP.lblNA());
        addSeparator();
        addInfoItem("lead", PB.lblGameInfoLead(), APP.lblNA());
        addSeparator();
        addInfoItem("move", PB.lblGameInfoPlayerMove(), APP.lblNA());
        addInfoItem("timeout", PB.lblGameRemainedTime(), APP.lblNA());
        addSeparator();
        addInfoItem("tonboard", PB.lbGameInfoTilesBoard(), APP.lblNA());
        addInfoItem("tinbank", PB.lblGameInfoTilesBank(), APP.lblNA());
        addInfoItem("tinhands", PB.lblGameInfoTilesHands(), APP.lblNA());
    }

    private void initData() {
        updateGameState();
        updateCurrentLead();
        updatePlayerTurn();
        updateLastMoveTime();
        updateTilesCount();
    }

    private void updateGameState() {
        GameboardItemBean.GameState newState = playboardItemBean.getGameState();
        switch (newState) {
            case WAITING:
                setInfoValue("started", PB.lblGameStateWaiting());
                break;
            case RUNNING:
                setInfoValue("started", DATE_TIME_FORMAT.format(new Date(playboardItemBean.getStartedTime())));
                break;
            case FINISHED:
                setInfoValue("started", "<b>" + PB.lblGameStateFinished() + "</b>");
                break;
            default:
                setInfoValue("started", APP.lblNA());
        }
    }

    private void updateCurrentLead() {
        int maxScore = 0;
        PlayerInfoBean currentLead = null;

        final PlayerInfoBean[] playerInfoBeans = playboardItemBean.getPlayers();
        for (PlayerInfoBean playerInfoBean : playerInfoBeans) {
            if (playerInfoBean == null) {
                currentLead = null;
                break;
            }
            final int score = playerInfoBean.getCurrentRating();
            if (score > maxScore) {
                currentLead = playerInfoBean;
                maxScore = score;
            } else if (score == maxScore) {
                currentLead = null;
            }
        }
        setInfoValue("lead", PlayerGridRenderer.getPlayerInfoHtml(currentLead, PB.lblNoCurrentLead(), false, true, ""));
    }

    private void updatePlayerTurn() {
        final GameboardItemBean.GameState state = playboardItemBean.getGameState();
        if (state == GameboardItemBean.GameState.FINISHED || state == GameboardItemBean.GameState.WAITING) {
            setInfoValue("move", PB.lblGameStateFinished());
        } else {
            final PlayerInfoBean player = playboardItemBean.getPlayerInfoBean(playboardItemBean.getPlayerMove());
            setInfoValue("move", PlayerMoveRenderer.getPlayerMoveHtml(currentPlayer, player, APP.lblNoOpponent(), false, true, ""));
        }
    }

    private void updateLastMoveTime() {
        final GameboardItemBean.GameState state = playboardItemBean.getGameState();
        if (state == GameboardItemBean.GameState.FINISHED || state == GameboardItemBean.GameState.WAITING) {
            setInfoValue("timeout", PB.lblGameStateFinished());
        } else {
            final long lastMoveTime = playboardItemBean.getLastMoveTime();
            if (lastMoveTime != 0) {
                setInfoValue("timeout", TimeFormatter.convertTimeout(playboardItemBean.getMinutesToTimeout()));
            } else {
                setInfoValue("timeout", APP.lblNA());
            }
        }
    }

    private void updateTilesCount() {
        final int boardTiles = scribbleBoard.getBoardTilesCount();
        final int handTiles = playboardItemBean.getTilesInHands();
        final int bankTiles = playboardItemBean.getBankCapacity() - boardTiles - handTiles;

        setInfoValue("tinbank", String.valueOf(bankTiles));
        setInfoValue("tonboard", String.valueOf(boardTiles));
        setInfoValue("tinhands", String.valueOf(handTiles));
    }

    private class ThePropertyChangeListener implements PropertyChangeListener<PlayboardItemBean> {
        public void propertyChanged(PlayboardItemBean bean, String property, Object oldValue, Object newValue) {
            if ("gameState".equals(property)) {
                updateGameState();
                updatePlayerTurn();
                updateLastMoveTime();
            } else if ("playerMove".equals(property)) {
                updateCurrentLead();
                updatePlayerTurn();
            } else if ("lastMoveTime".equals(property)) {
                updateLastMoveTime();
            } else if ("playersTilesCount".equals(property)) {
                updateTilesCount();
            } else if ("playerScorePoints".equals(property)) {
                updateCurrentLead();
            }
        }
    }

    private class TheBoardPropertiesListener implements PropertyChangeListener<ScribbleBoard> {
        public void propertyChanged(ScribbleBoard bean, String property, Object oldValue, Object newValue) {
            if ("boardTilesCount".equals(property)) {
                updateTilesCount();
            }
        }
    }
*/
}
