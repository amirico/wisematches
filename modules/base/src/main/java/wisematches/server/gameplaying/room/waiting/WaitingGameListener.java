package wisematches.server.gameplaying.room.waiting;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface WaitingGameListener {
	void gameWaitingOpened(WaitingGameInfo gameInfo);

	void gameWaitingUpdated(WaitingGameInfo gameInfo);

	void gameWaitingClosed(WaitingGameInfo gameInfo);
}
