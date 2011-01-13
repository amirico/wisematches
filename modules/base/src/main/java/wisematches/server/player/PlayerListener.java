package wisematches.server.player;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerListener {
	void playerUpdated(Player oldInfo, Player newInfo);
}
