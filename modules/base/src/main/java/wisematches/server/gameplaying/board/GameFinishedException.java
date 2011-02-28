package wisematches.server.gameplaying.board;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GameFinishedException extends GameStateException {
	private final GameState gameState;

	public GameFinishedException(GameState gameState) {
		super("Game already finished with status " + gameState);
		this.gameState = gameState;
	}

	public GameState getGameState() {
		return gameState;
	}
}
