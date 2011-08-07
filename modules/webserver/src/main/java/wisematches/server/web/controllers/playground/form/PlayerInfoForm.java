package wisematches.server.web.controllers.playground.form;

import wisematches.personality.Language;
import wisematches.personality.Membership;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerInfoForm {
    private final long playerId;
    private final String nickname;
    private final int activeGames;
    private final int finishedGames;
    private final int rating;
    private final String language;
    private final String membership;
    private final String averageMoveTime;

    public PlayerInfoForm(long id, String nickname, Membership membership, Language language, short rating, int activeGames, int finishedGames, String averageMoveTime) {
        this.playerId = id;
        this.nickname = nickname;
        this.activeGames = activeGames;
        this.finishedGames = finishedGames;
        this.averageMoveTime = averageMoveTime;
        this.language = language.code();
        this.membership = membership.name();
        this.rating = rating;
    }

    public long getPlayerId() {
        return playerId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getLanguage() {
        return language;
    }

    public String getMembership() {
        return membership;
    }

    public int getRating() {
        return rating;
    }

    public int getActiveGames() {
        return activeGames;
    }

    public int getFinishedGames() {
        return finishedGames;
    }

    public String getAverageMoveTime() {
        return averageMoveTime;
    }
}
