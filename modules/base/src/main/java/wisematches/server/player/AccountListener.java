package wisematches.server.player;

/**
 * @author klimese
 */
public interface AccountListener {
	void accountCreated(Player player);

	void accountUpdated(Player oldPlayer, Player newPlayer);

	void accountRemove(Player player);
}
