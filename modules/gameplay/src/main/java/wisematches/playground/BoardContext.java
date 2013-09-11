package wisematches.playground;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class BoardContext {
	private final boolean active;

	public BoardContext(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}
}
