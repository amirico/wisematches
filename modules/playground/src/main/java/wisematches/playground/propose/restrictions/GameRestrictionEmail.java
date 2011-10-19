package wisematches.playground.propose.restrictions;

import wisematches.personality.player.Player;
import wisematches.playground.propose.GameRestriction;
import wisematches.playground.propose.ViolatedRestrictionException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class GameRestrictionEmail implements GameRestriction {
	private final String email;

	private static final long serialVersionUID = -8668714084495105235L;

	public GameRestrictionEmail(String email) {
		this.email = email;
	}

	@Override
	public void validatePlayer(Player player) throws ViolatedRestrictionException {
		final String actual = player.getEmail();
		if (!actual.equalsIgnoreCase(email)) {
			throw new ViolatedRestrictionException("email", actual, email, this);
		}
	}
}
