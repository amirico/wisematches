/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */
package wisematches.client.gwt.app.client.content.stats;

import java.io.Serializable;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class SiteStatisticBean implements Serializable {
    private int registredPlayers;
    private int onlinePlayers;
    private int completedGames;
    private int gamesInProgress;

    public int getRegistredPlayers() {
        return registredPlayers;
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public int getCompletedGames() {
        return completedGames;
    }

    public int getGamesInProgress() {
        return gamesInProgress;
    }

    public void setRegistredPlayers(int registredPlayers) {
        this.registredPlayers = registredPlayers;
    }

    public void setOnlinePlayers(int onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public void setCompletedGames(int completedGames) {
        this.completedGames = completedGames;
    }

    public void setGamesInProgress(int gamesInProgress) {
        this.gamesInProgress = gamesInProgress;
    }

    @Override
    public String toString() {
        return "SiteStatisticBean{" +
                "registredPlayers=" + registredPlayers +
                ", onlinePlayers=" + onlinePlayers +
                ", completedGames=" + completedGames +
                ", gamesInProgress=" + gamesInProgress +
                '}';
    }
}
