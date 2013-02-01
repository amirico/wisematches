package wisematches.core;

import java.util.TimeZone;

/**
 * The {@code Player} represents real player.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class Player extends Personality {
	protected Player(long id) {
		super(id);
	}

	public abstract String getEmail();

	public abstract String getNickname();

	public abstract Language getLanguage();

	public abstract TimeZone getTimeZone();

	public abstract PlayerType getPlayerType();
}
