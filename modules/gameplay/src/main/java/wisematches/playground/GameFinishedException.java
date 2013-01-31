package wisematches.playground;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GameFinishedException extends GameStateException {
	private final GameResolution gameResolution;

	public GameFinishedException(GameResolution gameResolution) {
		super("Game already finished with resolution: " + gameResolution);
		this.gameResolution = gameResolution;
	}

	public GameResolution getGameResolution() {
		return gameResolution;
	}
}
