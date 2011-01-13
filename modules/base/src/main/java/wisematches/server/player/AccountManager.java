package wisematches.server.player;

/**
 * <code>AccountManager</code> allows create and authentificate players or does search by some criteria.
 * <p/>
 * {@code AccountManager} does not extends {@code PlayerManager} because {@code PlayerManager} has a lot
 * of implementation.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface AccountManager extends PlayerManager {
	void addAccountListener(AccountListener l);

	void removeAccountListener(AccountListener l);


	/**
	 * Creates new player based on information in specified player object. This method does not
	 * modify original object and returns new object instead.
	 *
	 * @param player the player to be created.
	 * @return created player object possible the same but can be new one so returned object must be
	 *         used after this method was called.
	 * @throws DuplicateAccountException	 if account with specified username or email already exist.
	 * @throws InadmissibleUsernameException if select username can't be used by User Naming Policy
	 */
	Player createPlayer(Player player) throws DuplicateAccountException, InadmissibleUsernameException;

	/**
	 * Updates specified player. The player must be
	 *
	 * @param player the player with new information.
	 * @throws UnknownAccountException   if an account for specified player is unknown.
	 * @throws DuplicateAccountException if account with specified username or email already exist.
	 */
	void updatePlayer(Player player) throws UnknownAccountException, DuplicateAccountException, InadmissibleUsernameException;

	/**
	 * Removes specified player.
	 *
	 * @param player the player to be removed.
	 * @throws UnknownAccountException if an account for specified player is unknown.
	 */
	void removePlayer(Player player) throws UnknownAccountException;

	/**
	 * Checks is account with specified username and email can be created or not.
	 *
	 * @param username the username to be checked
	 * @param email	the email to be checked.
	 * @return the {@code AccountAvailability} object that contains information about availability.
	 */
	AccountAvailability checkAccountAvailable(String username, String email);
}