package wisematches.playground.tournament;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface AnnouncementListener {
	void tournamentAnnounced(Announcement closedAnnouncement, Announcement newAnnouncement);

	/**
	 * Indicates that new player subscribed to a tournament.
	 *
	 * @param request original player's request.
	 */
	void playerSubscribed(AnnouncementSubscription request);

	/**
	 * Indicates that new a unsubscribed from a tournament.
	 *
	 * @param request original player's request.
	 */
	void playerUnsubscribed(AnnouncementSubscription request);
}
