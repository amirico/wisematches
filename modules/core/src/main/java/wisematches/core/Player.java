package wisematches.core;

/**
 * The {@code Player} represents real player.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class Player extends Personality {
	protected Player(long id) {
		super(id);
	}

	public abstract String getNickname();

	public abstract Language getLanguage();

	public abstract PlayerType getPlayerType();
}
