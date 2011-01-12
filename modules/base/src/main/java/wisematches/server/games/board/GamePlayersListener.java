package wisematches.server.games.board;

import wisematches.kernel.player.Player;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface GamePlayersListener {
	void playerAdded(GameBoard board, Player player);

	void playerRemoved(GameBoard board, Player player);
}
