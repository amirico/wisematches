package wisematches.playground;

/**
 * {@code GameSettingsProvider} creates new game settings based on it's internal logic and bechaviour.
 * <p/>
 * For example, {@code GameSettingsProvider} creates new settings to tournament games.
 *
 * @param <S> game settings type
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameSettingsProvider<S extends GameSettings> {
	/**
	 * Returns default game settings created by this provider.
	 *
	 * @return the default game settings created by this provider.
	 */
	S getDefaultSettings();

	/**
	 * Creates new games settings based on specified parameters
	 *
	 * @return new game settings
	 */
	S createGameSettings();
}
