package wisematches.playground.tournament.upcoming;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentAnnouncementListener {
	void tournamentAnnounced(TournamentAnnouncement closedAnnouncement, TournamentAnnouncement newAnnouncement);
}
