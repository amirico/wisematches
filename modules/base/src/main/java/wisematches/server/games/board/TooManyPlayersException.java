package wisematches.server.games.board;

/**
 * This exception indicates that
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TooManyPlayersException extends RuntimeException {
	private final int maxPlayers;

	public TooManyPlayersException(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}
}
