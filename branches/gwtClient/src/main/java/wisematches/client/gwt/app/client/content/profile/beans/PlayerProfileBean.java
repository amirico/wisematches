package wisematches.client.gwt.app.client.content.profile.beans;

import wisematches.client.gwt.app.client.content.player.PlayerInfoBean;

import java.io.Serializable;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerProfileBean implements Serializable {
    private PlayerInfoBean playerInfoBean;
    private long registredFrom;
    private int place;
    private int activeGames;
    private int wonGames;
    private int lostGames;
    private int drawGames;
    private int timeouts;
    private int turnsCount;
    private int averageTurnTime;
    private int lastMoveAgo;
    private int lastSeenOnlineAgo;

    private RatingHistoryBean ratingHistoryBean;
    private PlayerSettingsBean playerSettingsBean = new PlayerSettingsBean();

    private PlayerRatingBean allGamesInfo;
    private PlayerRatingBean thirtyDaysRaing;
    private PlayerRatingBean ninetyDaysRaing;
    private PlayerRatingBean yearRaing;

    public PlayerProfileBean() {
    }

    public PlayerInfoBean getPlayerInfoBean() {
        return playerInfoBean;
    }

    public void setPlayerInfoBean(PlayerInfoBean playerInfoBean) {
        this.playerInfoBean = playerInfoBean;
    }

    public long getRegistredFrom() {
        return registredFrom;
    }

    public void setRegistredFrom(long registredFrom) {
        this.registredFrom = registredFrom;
    }

    public int getLastSeenOnlineAgo() {
        return lastSeenOnlineAgo;
    }

    public void setLastSeenOnlineAgo(int lastSeenOnlineAgo) {
        this.lastSeenOnlineAgo = lastSeenOnlineAgo;
    }

    public int getTotalGames() {
        return wonGames + lostGames + drawGames;
    }

    public int getActiveGames() {
        return activeGames;
    }

    public void setActiveGames(int activeGames) {
        this.activeGames = activeGames;
    }

    public int getWonGames() {
        return wonGames;
    }

    public void setWonGames(int wonGames) {
        this.wonGames = wonGames;
    }

    public int getLostGames() {
        return lostGames;
    }

    public void setLostGames(int lostGames) {
        this.lostGames = lostGames;
    }

    public int getDrawGames() {
        return drawGames;
    }

    public void setDrawGames(int drawGames) {
        this.drawGames = drawGames;
    }

    public int getTimeouts() {
        return timeouts;
    }

    public void setTimeouts(int timeouts) {
        this.timeouts = timeouts;
    }

    public int getTurnsCount() {
        return turnsCount;
    }

    public void setTurnsCount(int turnsCount) {
        this.turnsCount = turnsCount;
    }

    public int getAverageTurnTime() {
        return averageTurnTime;
    }

    public void setAverageTurnTime(int averageTurnTime) {
        this.averageTurnTime = averageTurnTime;
    }

    public int getLastMoveAgo() {
        return lastMoveAgo;
    }

    public void setLastMoveAgo(int lastMoveAgo) {
        this.lastMoveAgo = lastMoveAgo;
    }

    public PlayerRatingBean getAllGamesInfo() {
        return allGamesInfo;
    }

    public void setAllGamesInfo(PlayerRatingBean allGamesInfo) {
        this.allGamesInfo = allGamesInfo;
    }

    public PlayerRatingBean getThirtyDaysRaing() {
        return thirtyDaysRaing;
    }

    public void setThirtyDaysRaing(PlayerRatingBean thirtyDaysRaing) {
        this.thirtyDaysRaing = thirtyDaysRaing;
    }

    public PlayerRatingBean getNinetyDaysRaing() {
        return ninetyDaysRaing;
    }

    public void setNinetyDaysRaing(PlayerRatingBean ninetyDaysRaing) {
        this.ninetyDaysRaing = ninetyDaysRaing;
    }

    public PlayerRatingBean getYearRaing() {
        return yearRaing;
    }

    public void setYearRaing(PlayerRatingBean yearRaing) {
        this.yearRaing = yearRaing;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public PlayerSettingsBean getPlayerSettingsBean() {
        return playerSettingsBean;
    }

    public RatingHistoryBean getRatingHistoryBean() {
        return ratingHistoryBean;
    }

    public void setRatingHistoryBean(RatingHistoryBean ratingHistoryBean) {
        this.ratingHistoryBean = ratingHistoryBean;
    }

    @Override
    public String toString() {
        return "PlayerProfileBean{" +
                "playerInfoBean=" + playerInfoBean +
                ", registredFrom=" + registredFrom +
                ", place=" + place +
                ", activeGames=" + activeGames +
                ", wonGames=" + wonGames +
                ", lostGames=" + lostGames +
                ", drawGames=" + drawGames +
                ", timeouts=" + timeouts +
                ", turnsCount=" + turnsCount +
                ", averageTurnTime=" + averageTurnTime +
                ", lastMoveAgo=" + lastMoveAgo +
                ", lastSeenOnlineAgo=" + lastSeenOnlineAgo +
                ", ratingHistoryBean=" + ratingHistoryBean +
                ", playerSettingsBean=" + playerSettingsBean +
                ", thirtyDaysRaing=" + thirtyDaysRaing +
                ", ninetyDaysRaing=" + ninetyDaysRaing +
                ", yearRaing=" + yearRaing +
                ", allGamesInfo=" + allGamesInfo +
                '}';
    }
}
