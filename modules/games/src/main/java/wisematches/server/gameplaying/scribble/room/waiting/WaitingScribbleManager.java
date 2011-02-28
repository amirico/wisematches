package wisematches.server.gameplaying.scribble.room.waiting;

import wisematches.server.gameplaying.room.waiting.AbstractWaitingGameManager;
import wisematches.server.player.Player;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WaitingScribbleManager extends AbstractWaitingGameManager<WaitingScribbleInfo> {
	private final Map<Long, WaitingScribbleInfo> infoMap = new ConcurrentHashMap<Long, WaitingScribbleInfo>();

	public WaitingScribbleManager() {
	}

	@Override
	public synchronized void openWaitingGame(WaitingScribbleInfo info) {
		infoMap.put(info.getId(), info);
		fireGameWaitingOpened(info);
	}

	@Override
	public synchronized WaitingScribbleInfo attachPlayer(long waitingId, Player player) {
		final WaitingScribbleInfo info = infoMap.get(waitingId);
		if (info == null) {
			throw new UnsupportedOperationException("Checking is not implemented");
		}
		if (!info.isSuitPlayer(player)) {
			throw new UnsupportedOperationException("Checking is not implemented");
		}
		info.attachPlayer(player);
		fireGameWaitingUpdated(info);
		if (info.isGameReady()) { // if game is ready - close it.
			closeWaitingGame(info);
		}
		return info;
	}

	@Override
	public synchronized WaitingScribbleInfo detachPlayer(long waitingId, Player player) {
		final WaitingScribbleInfo info = infoMap.get(waitingId);
		if (info == null) {
			throw new UnsupportedOperationException("Checking is not implemented");
		}
		info.detachPlayer(player);
		fireGameWaitingUpdated(info);
		return info;
	}

	@Override
	public synchronized void closeWaitingGame(WaitingScribbleInfo waitingGame) {
		infoMap.remove(waitingGame.getId());
		fireGameWaitingClosed(waitingGame);
	}

	@Override
	public synchronized Collection<WaitingScribbleInfo> getWaitingGames() {
		return infoMap.values();
	}
}
