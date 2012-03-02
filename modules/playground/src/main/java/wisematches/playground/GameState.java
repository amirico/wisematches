package wisematches.playground;

/**
 * Indicates current games state.
 * <p/>
 * There are only two possible states: game can be active or closed.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum GameState {
	/**
	 * Indicates that game is active.
	 */
	ACTIVE,
	/**
	 * Indicates that game was finished not by timeout
	 */
	FINISHED,
	/**
	 * Indicates that game was finished by timeout
	 */
	INTERRUPTED
}
