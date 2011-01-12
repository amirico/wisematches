package wisematches.server.player;

/**
 * @author klimese
 */
public interface AccountManager extends PlayerManager {
	void addAccountListener(AccountListener l);

	void removeAccountListener(AccountListener l);


	Player createPlayer(Player player);

	Player updatePlayer(Player player);

	void removePlayer(Player player);
}