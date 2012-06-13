package wisematches.playground.tournament;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentSubscriptionListener {
    void tournamentAnnounced(Announcement closedAnnouncement, Announcement newAnnouncement);

    /**
     * Indicates that new player subscribed to a tournament.
     *
     * @param request original player's request.
     */
    void playerSubscribed(TournamentSubscription request);

    /**
     * Indicates that new a unsubscribed from a tournament.
     *
     * @param request original player's request.
     */
    void playerUnsubscribed(TournamentSubscription request);
}
