package wisematches.playground;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GameFinishedException extends GameStateException {
	private final GameResolution resolution;

	public GameFinishedException(GameResolution resolution) {
		super("Game already finished with resolution: " + resolution);
		this.resolution = resolution;
	}

	public GameResolution getResolution() {
		return resolution;
	}
}
