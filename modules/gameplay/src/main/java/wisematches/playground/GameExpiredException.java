package wisematches.playground;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GameExpiredException extends GameStateException {
	public GameExpiredException() {
		super("Game expired and no one move can be done.");
	}
}
