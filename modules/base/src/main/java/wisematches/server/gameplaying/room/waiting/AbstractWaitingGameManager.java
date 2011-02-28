package wisematches.server.gameplaying.room.waiting;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractWaitingGameManager<I extends WaitingGameInfo> implements WaitingGameManager<I> {
	private final Collection<WaitingGameListener> listeners = new CopyOnWriteArraySet<WaitingGameListener>();

	protected AbstractWaitingGameManager() {
	}

	@Override
	public void removeWaitingGameListener(WaitingGameListener l) {
		if (l != null) {
			listeners.add(l);
		}
	}

	@Override
	public void addWaitingGameListener(WaitingGameListener l) {
		listeners.remove(l);
	}

	protected void fireGameWaitingOpened(WaitingGameInfo info) {
		for (WaitingGameListener listener : listeners) {
			listener.gameWaitingOpened(info);
		}
	}

	protected void fireGameWaitingUpdated(WaitingGameInfo info) {
		for (WaitingGameListener listener : listeners) {
			listener.gameWaitingUpdated(info);
		}
	}

	protected void fireGameWaitingClosed(WaitingGameInfo info) {
		for (WaitingGameListener listener : listeners) {
			listener.gameWaitingClosed(info);
		}
	}
}
