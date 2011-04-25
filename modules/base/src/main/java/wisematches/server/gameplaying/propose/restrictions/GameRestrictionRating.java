package wisematches.server.gameplaying.propose.restrictions;

import wisematches.server.gameplaying.propose.GameRestriction;
import wisematches.server.gameplaying.propose.ViolatedRestrictionException;
import wisematches.server.personality.player.Player;

/**
 * Validates player's rating and throws {@code ViolatedRestrictionException} with code {@code rating.min}
 * if player's rating to low or {@code rating.max} if player's rating too hi.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class GameRestrictionRating implements GameRestriction {
	private final int min;
	private final int max;

	public GameRestrictionRating(int min, int max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public void validatePlayer(Player player) throws ViolatedRestrictionException {
		final short rating = player.getRating();
		if (rating < min) {
			throw new ViolatedRestrictionException("rating.min", rating, min, this);
		}
		if (rating > max) {
			throw new ViolatedRestrictionException("rating.max", rating, max, this);
		}
	}
}
