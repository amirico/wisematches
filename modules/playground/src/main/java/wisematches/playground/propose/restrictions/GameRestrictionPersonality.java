package wisematches.playground.propose.restrictions;

import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.playground.propose.GameRestriction;
import wisematches.playground.propose.ViolatedRestrictionException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class GameRestrictionPersonality implements GameRestriction {
	private final long playerId;

	private static final long serialVersionUID = -8523418042927916445L;

	public GameRestrictionPersonality(Personality personality) {
		this.playerId = personality.getId();
	}

	@Override
	public void validatePlayer(Player player) throws ViolatedRestrictionException {
		final long actual = player.getId();
		if (actual != playerId) {
			throw new ViolatedRestrictionException("playerId", actual, playerId, this);
		}
	}
}