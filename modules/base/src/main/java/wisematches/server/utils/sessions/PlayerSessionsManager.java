package wisematches.server.utils.sessions;

import wisematches.server.player.Player;

import java.util.Collection;

/**
 * Sessions manager contains session beans of players. Each player can have
 * at the same time a lot of beans linked to the same session.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface PlayerSessionsManager {
	/**
	 * Adds new sessions listener to this manager.
	 *
	 * @param l the sessions listener to be added.
	 */
	void addPlayerSessionsListener(PlayerSessionsListener l);

	/**
	 * Removes sessions listener from this manager.
	 *
	 * @param l the sessions listener to be removed.
	 */
	void removePlayerSessionsListener(PlayerSessionsListener l);

	/**
	 * Adds new player's state listener to this manager.
	 *
	 * @param l the state listener to be added.
	 */
	void addPlayerOnlineStateListener(PlayerOnlineStateListener l);

	/**
	 * Removes sessions listener from this manager.
	 *
	 * @param l the sessions listener to be removed.
	 */
	void removePlayerOnlineStateListener(PlayerOnlineStateListener l);

	/**
	 * Returns player bean associated with specified session key.
	 * <p/>
	 * This method returns player session bean depends on type of variable that takes result of method call.
	 * <p/>
	 * Not any interfaces are supported by this session manager. For more details about supported interfaces see
	 * documentation of using implementation.
	 *
	 * @param sessionKey the session key.
	 * @param <T>        the type of player session bean. Any interface that extends
	 *                   {@code PlayerSessionBean} interface and is registren in this session manager.
	 *                   <p/>
	 *                   How to register iterface in {@code PlayerSessionsManager} see your implementation for more
	 *                   details.
	 * @return player session bean or {@code null} if there is no session with specified key.
	 * @throws NullPointerException if {@code sessionKey} is {@code null}
	 * @throws ClassCastException   if player session bean does not support requred type.
	 */
	<T extends PlayerSessionBean> T getPlayerSessionBean(String sessionKey);

	/**
	 * Returns unmidifiable collection of all session beans for specified player.
	 * <p/>
	 * If player has no one session empty collection will be returned.
	 *
	 * @param player
	 * @param <T>
	 * @return
	 */
	<T extends PlayerSessionBean> Collection<T> getPlayerSessionBeans(Player player);

	/**
	 * Returns collection of all session beans.
	 *
	 * @param <T>
	 * @return
	 */
	<T extends PlayerSessionBean> Collection<T> getPlayerSessionBeans();

	/**
	 * Returns unmodifiable collection of online players.
	 *
	 * @return the unmodifiable collection of players
	 */
	Collection<Player> getOnlinePlayers();

	/**
	 * Checks that specified player is online at this moment.
	 *
	 * @param player the player to be checked
	 * @return {@code true} if player is online at this moment; {@code false} - otherwise.
	 */
	boolean isPlayerOnline(Player player);
}