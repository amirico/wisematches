package wisematches.playground.tourney;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneyEntityListener<E extends TourneyEntity> {
	void entityStarted(E entity);

	void entityFinished(E entity);
}
