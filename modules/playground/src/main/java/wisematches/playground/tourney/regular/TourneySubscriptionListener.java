package wisematches.playground.tourney.regular;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneySubscriptionListener {
	void subscribed(TourneySubscription subscription);

	void unsubscribed(TourneySubscription subscription);
}
