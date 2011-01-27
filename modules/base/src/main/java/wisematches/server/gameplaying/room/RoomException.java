package wisematches.server.gameplaying.room;

/**
 * Base exception for all room exceptions.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RoomException extends Exception {
	/**
	 * {@inheritDoc}
	 */
	public RoomException() {
	}

	/**
	 * {@inheritDoc}
	 */
	public RoomException(String message) {
		super(message);
	}

	/**
	 * {@inheritDoc}
	 */
	public RoomException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * {@inheritDoc}
	 */
	public RoomException(Throwable cause) {
		super(cause);
	}
}
