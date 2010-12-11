package wisematches.client.gwt.app.client.content.gameboard;

import wisematches.client.gwt.app.client.content.dashboard.DashboardItemBean;
import wisematches.client.gwt.app.client.util.TimeFormatter;

import java.io.Serializable;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GameboardItemBean<T extends GameboardItemBean> extends DashboardItemBean<T> implements Serializable {
    private long startedTime;
    private long finishedTime;
    private long playerMove;
    private long lastMoveTime;
    
    private static final int MILLIS_IN_DAY = 86400000;

    public GameboardItemBean() {
    }

    public long getPlayerMove() {
        return playerMove;
    }

    public void setPlayerMove(long playerMove) {
        long old = this.playerMove;
        this.playerMove = playerMove;
        firePropertyChanged("playerMove", old, playerMove);
    }

    public long getStartedTime() {
        return startedTime;
    }

    public void setStartedTime(long startedTime) {
        GameState gameState = getGameState();
        long old = this.startedTime;
        this.startedTime = startedTime;
        firePropertyChanged("startedTime", old, startedTime);
        firePropertyChanged("gameState", gameState, getGameState());
    }

    public long getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(long finishedTime) {
        GameboardItemBean.GameState gameState = getGameState();
        long old = this.finishedTime;
        this.finishedTime = finishedTime;
        firePropertyChanged("finishedTime", old, finishedTime);
        firePropertyChanged("gameState", gameState, getGameState());
    }

    /**
     * Returns time to left in minutes. If value is negative game isn't started yet.
     *
     * @return time to left in minutes.
     */
    public long getLastMoveTime() {
        return lastMoveTime;
    }

    public void setLastMoveTime(long lastMoveTime) {
        long old = this.lastMoveTime;
        this.lastMoveTime = lastMoveTime;
        firePropertyChanged("lastMoveTime", old, lastMoveTime);
    }

    /**
     * Returns time in milliseconds to time out. This time is
     *
     * @return the time to time out.
     */
    public int getMillisToTimeout() {
        if (lastMoveTime == 0) {
            return 0;
        }
        return getDaysPerMove() * MILLIS_IN_DAY - (int) (TimeFormatter.serverTimeMillis() - lastMoveTime);
    }

    /**
     * Returns time in milliseconds to time out. This time is
     *
     * @return the time to time out.
     */
    public int getMinutesToTimeout() {
        if (lastMoveTime == 0) {
            return 0;
        }
        return getDaysPerMove() * 24 * 60 - (int) (TimeFormatter.serverTimeMillis() - lastMoveTime) / 60000;
    }


    public GameState getGameState() {
        if (startedTime == 0) {
            return GameState.WAITING;
        } else if (finishedTime != 0) {
            return GameState.FINISHED;
        } else {
            return GameState.RUNNING;
        }
    }

    /**
     * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
     */
    public static enum GameState {
        WAITING,
        RUNNING,
        FINISHED
    }
}
