package wisematches.playground.tourney;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneyElementListener<E extends TourneyElement> {
	void tourneyEntityStarted(E element);

	void tourneyEntityFinished(E element);

	void tourneyEntityScheduled(E element);
}
