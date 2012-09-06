package wisematches.playground.tourney;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneyItemListener {
	void tourneyEntityStarted(TourneyEntity entity);

	void tourneyEntityFinished(TourneyEntity entity);

	void tourneyEntityScheduled(TourneyEntity entity);
}
