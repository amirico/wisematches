package wisematches.playground;

/**
 * Contains information about game resolution.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum GameResolution {
	/**
	 * Game has been finished by one of players.
	 */
	FINISHED,
	/**
	 * Game has been finished because players can't make new words.
	 */
	@Deprecated
	STALEMATE,
	/**
	 * Game time has expired and game has been finished by terminator.
	 */
	INTERRUPTED,
	/**
	 * One of players has resigned the game.
	 */
	RESIGNED
}
