package wisematches.playground.tourney;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneyEntityListener {
	void entityStarted(TourneyEntity entity);

	void entityFinished(TourneyEntity entity);
}
