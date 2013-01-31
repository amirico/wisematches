package wisematches.playground.tourney.regular;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface RegularTourneyListener {
	void tourneyAnnounced(Tourney tourney);

	void tourneyStarted(Tourney tourney);

	void tourneyFinished(Tourney tourney, TourneyDivision division);
}
