package wisematches.server.playground.expiration;

/**
 * This is very simple manager that does not have any methods and provides ability only for
 * listening game expiration information.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameExpirationManager {
	void addGameExpirationListener(GameExpirationListener l);

	void removeGameExpirationListener(GameExpirationListener l);
}
