package wisematches.server.robot.impl;

import wisematches.kernel.player.Player;
import wisematches.server.core.account.PlayerListener;
import wisematches.server.core.account.PlayerManager;
import wisematches.server.core.guest.GuestPlayerManager;
import wisematches.server.robot.RobotsBrainManager;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class CompositePlayerManager implements PlayerManager {
	private PlayerManager realPlayerManager;
	private RobotsBrainManager robotsBrainManager;
	private GuestPlayerManager guestPlayerManager;

	private final PlayerListener playerListener = new ThePlayerListener();
	private final Collection<PlayerListener> listeners = new CopyOnWriteArraySet<PlayerListener>();

	public CompositePlayerManager() {
	}

	public void addPlayerListener(PlayerListener l) {
		listeners.add(l);
	}

	public void removePlayerListener(PlayerListener l) {
		listeners.remove(l);
	}

	public void setRealPlayerManager(PlayerManager realPlayerManager) {
		if (this.realPlayerManager != null) {
			this.realPlayerManager.removePlayerListener(playerListener);
		}

		this.realPlayerManager = realPlayerManager;

		if (this.realPlayerManager != null) {
			this.realPlayerManager.addPlayerListener(playerListener);
		}
	}

	public void setRobotsBrainManager(RobotsBrainManager robotsBrainManager) {
		if (this.robotsBrainManager != null) {
			this.robotsBrainManager.removePlayerListener(playerListener);
		}

		this.robotsBrainManager = robotsBrainManager;

		if (this.robotsBrainManager != null) {
			this.robotsBrainManager.addPlayerListener(playerListener);
		}
	}

	public void setGuestPlayerManager(GuestPlayerManager guestPlayerManager) {
		if (this.guestPlayerManager != null) {
			this.guestPlayerManager.removePlayerListener(playerListener);
		}

		this.guestPlayerManager = guestPlayerManager;

		if (this.guestPlayerManager != null) {
			this.guestPlayerManager.addPlayerListener(playerListener);
		}
	}

	public Player getPlayer(long playerId) {
		if (guestPlayerManager.isGuestPlayer(playerId)) {
			return guestPlayerManager.getPlayer(playerId);
		} else if (robotsBrainManager.isRobotPlayer(playerId)) {
			return robotsBrainManager.getPlayer(playerId);
		}
		return realPlayerManager.getPlayer(playerId);
	}

	public void updatePlayer(Player player) {
		if (guestPlayerManager.isGuestPlayer(player)) {
			guestPlayerManager.updatePlayer(player);
		} else if (robotsBrainManager.isRobotPlayer(player)) {
			robotsBrainManager.updatePlayer(player);
		} else {
			realPlayerManager.updatePlayer(player);
		}
	}

	private class ThePlayerListener implements PlayerListener {
		public void playerUpdated(Player player) {
			for (PlayerListener listener : listeners) {
				listener.playerUpdated(player);
			}
		}
	}
}
