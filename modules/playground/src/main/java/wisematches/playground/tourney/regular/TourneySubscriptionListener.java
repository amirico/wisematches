package wisematches.playground.tourney.regular;

/**
 * Tracks user subscriptions as well as any internal updates for the user subscription.
 * <p/>
 * Please note that in case of manual resubscription two methods will be invoked: unsubscribed and
 * when subscribe again but if subscription was changed by manager, when resubsribed method will be invoked.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneySubscriptionListener {
	/**
	 * Indicates that a player has been subscribed to a tourney.
	 *
	 * @param subscription information about subscribed player and subscription details.
	 */
	void subscribed(TourneySubscription subscription);

	/**
	 * Indicates that a player has been unsubscribed from a tourney.
	 *
	 * @param subscription subscription information
	 */
	void unsubscribed(TourneySubscription subscription);

	/**
	 * Indicates that subscription section was changed for the speciaied player.
	 *
	 * @param player     the player id
	 * @param tourney    the tourney number
	 * @param oldSection old section.
	 * @param newSection new section. Can be null if player is moved ourside tourney because there is no
	 *                   appropriate group where it can play.
	 */
	void resubscribed(long player, int tourney, TourneySection oldSection, TourneySection newSection);
}
