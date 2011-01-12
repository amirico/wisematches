package wisematches.server.games.cleaner;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface GameTimeoutListener {
	/**
	 * This method is invoked when time is running out.
	 *
	 * @param event the associated event.
	 */
	void timeIsRunningOut(GameTimeoutEvent event);

	/**
	 * This method invoked when time is up and game was terminated.
	 *
	 * @param event the associated event.
	 */
	void timeIsUp(GameTimeoutEvent event);
}
