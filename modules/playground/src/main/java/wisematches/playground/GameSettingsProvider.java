package wisematches.playground;

/**
 * {@code GameSettingsProvider} creates new game settings based on it's internal logic and bechaviour.
 * <p/>
 * For example, {@code GameSettingsProvider} creates new settings to tournament games.
 *
 * @param <S> game settings type
 * @param <R> any object that is supported by this provide. New settings will be created based on specified request object.
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameSettingsProvider<S extends GameSettings, R> {
	/**
	 * Creates new games settings based on specified parameters
	 *
	 * @return new game settings
	 */
	S createGameSettings(R request);
}
