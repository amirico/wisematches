package wisematches.server.player.computer;

import wisematches.server.player.Player;
import wisematches.server.player.PlayerListener;
import wisematches.server.player.PlayerManager;

import java.util.Collection;

/**
 * {@code PlayerManager} implementation that interacts with {@code ComputerPlayer} object
 * and has access to any computer players.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ComputerPlayerManager implements PlayerManager {
	public ComputerPlayerManager() {
	}

	@Override
	public void addPlayerListener(PlayerListener listener) {
	}

	@Override
	public void removePlayerListener(PlayerListener listener) {
	}

	@Override
	public Player getPlayer(long playerId) {
		return ComputerPlayer.getComputerPlayer(playerId);
	}

	public <T extends ComputerPlayer> Collection<T> getPlayers(Class<T> playerType) {
		return ComputerPlayer.getComputerPlayers(playerType);
	}

	@Override
	public Player findByEmail(String email) {
		return null;
	}

	@Override
	public Player findByUsername(String username) {
		return null;
	}
}
