package wisematches.server.games.board;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GameFinishedException extends GameMoveException {
	private final GameState gameState;

	public GameFinishedException(GameState gameState) {
		super("Game already finished with status " + gameState);
		this.gameState = gameState;
	}

	public GameState getGameState() {
		return gameState;
	}
}
