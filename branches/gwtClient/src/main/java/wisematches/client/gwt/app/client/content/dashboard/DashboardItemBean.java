package wisematches.client.gwt.app.client.content.dashboard;

import wisematches.client.gwt.app.client.content.player.PlayerInfoBean;
import wisematches.client.gwt.core.client.beans.PropertyChangeSupport;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class DashboardItemBean<T extends DashboardItemBean> extends PropertyChangeSupport<T> implements Serializable {
    private long boardId;
    private String title;
    private String locale;
    private int daysPerMove;
    private int minRating;
    private int maxRating;
    private PlayerInfoBean[] players;

    public DashboardItemBean() {
    }

    public DashboardItemBean(String title, PlayerInfoBean[] players, String locale, int daysPerMove) {
        this.title = title;
        this.players = players;
        this.locale = locale;
        this.daysPerMove = daysPerMove;
    }

    public DashboardItemBean(long boardId, String title, PlayerInfoBean[] players, String locale, int daysPerMove) {
        this.boardId = boardId;
        this.title = title;
        this.players = players;
        this.locale = locale;
        this.daysPerMove = daysPerMove;
    }

    public long getBoardId() {
        return boardId;
    }

    public void setBoardId(long boardId) {
        long oldId = this.boardId;
        this.boardId = boardId;
        firePropertyChanged("boardId", oldId, boardId);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        String oldTitle = this.title;
        this.title = title;
        firePropertyChanged("title", oldTitle, title);
    }


    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        String old = this.locale;
        this.locale = locale;
        firePropertyChanged("locale", old, locale);
    }

    public PlayerInfoBean[] getPlayers() {
        return players;
    }

    public void setPlayers(PlayerInfoBean[] players) {
        PlayerInfoBean[] old = this.players;
        this.players = players;
        firePropertyChanged("players", old, players);
    }

    public void addOpponent(PlayerInfoBean player) {
        final PlayerInfoBean[] beans = getPlayers();
        for (int i = 0; i < beans.length; i++) {
            if (beans[i] == null) {
                beans[i] = player;
            }
        }
        setPlayers(beans);
    }

    public void removeOpponent(PlayerInfoBean player) {
        final PlayerInfoBean[] beans = getPlayers();
        for (int i = 0; i < beans.length; i++) {
            if (beans[i] == player) {
                beans[i] = null;
            }
        }
        setPlayers(beans);
    }

    public PlayerInfoBean getPlayerInfoBean(long playerId) {
        for (PlayerInfoBean player : players) {
            if (player != null && player.getPlayerId() == playerId) {
                return player;
            }
        }
        return null;
    }

    public int getDaysPerMove() {
        return daysPerMove;
    }

    public void setDaysPerMove(int daysPerMove) {
        long old = this.daysPerMove;
        this.daysPerMove = daysPerMove;
        firePropertyChanged("daysPerMove", old, daysPerMove);
    }

    public int getMinRating() {
        return minRating;
    }

    public void setMinRating(int minRating) {
        int old = this.minRating;
        this.minRating = minRating;
        firePropertyChanged("minRating", old, minRating);
    }

    public int getMaxRating() {
        return maxRating;
    }

    public void setMaxRating(int maxRating) {
        int old = this.maxRating;
        this.maxRating = maxRating;
        firePropertyChanged("maxRating", old, maxRating);
    }

    @Override
    public String toString() {
        return "DashboardItemBean{" +
                "boardId=" + boardId +
                ", title='" + title + '\'' +
                ", players=" + (players == null ? null : Arrays.asList(players)) +
                ", locale='" + locale + '\'' +
                ", daysPerMove=" + daysPerMove +
                ", minRating=" + minRating +
                ", maxRating=" + maxRating +
                '}';
    }
}
