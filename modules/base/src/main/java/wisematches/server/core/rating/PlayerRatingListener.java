package wisematches.server.core.rating;

/**
 * This listener is used in {@code RatingsManager} to notify clients about players ratings.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface PlayerRatingListener {
    /**
     * This method is invoked when player's rating was changed after a game. Specified player already
     * contains new rating.
     *
     * @param event the player who rating was changed.
     */
    void playerRaitingChanged(PlayerRatingEvent event);
}
