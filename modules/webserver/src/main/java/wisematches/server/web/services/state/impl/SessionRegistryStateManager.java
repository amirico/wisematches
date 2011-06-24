package wisematches.server.web.services.state.impl;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistryImpl;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.server.security.impl.WMUserDetails;
import wisematches.server.web.services.state.PlayerStateListener;
import wisematches.server.web.services.state.PlayerStateManager;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SessionRegistryStateManager extends SessionRegistryImpl implements PlayerStateManager {
	private final Collection<PlayerStateListener> listeners = new CopyOnWriteArraySet<PlayerStateListener>();

	public SessionRegistryStateManager() {
	}

	@Override
	public void addPlayerStateListener(PlayerStateListener l) {
		if (l != null) {
			listeners.add(l);
		}
	}

	@Override
	public void removePlayerStateListener(PlayerStateListener l) {
		listeners.remove(l);
	}

	@Override
	public boolean isPlayerOnline(Personality personality) {
		return !getAllSessions(personality, false).isEmpty();
	}

	@Override
	public void registerNewSession(String sessionId, Object principal) {
		super.registerNewSession(sessionId, principal);

		final SessionInformation info = getSessionInformation(sessionId);
		if (info != null && info.getPrincipal() instanceof WMUserDetails) {
			final Player player = ((WMUserDetails) info.getPrincipal()).getPlayer();
			// notify listeners only about first session
			if (getAllSessions(player, true).size() == 1) {
				for (PlayerStateListener listener : listeners) {
					listener.playerOnline(player);
				}
			}
		}
	}

	@Override
	public void refreshLastRequest(String sessionId) {
		super.refreshLastRequest(sessionId);

		final SessionInformation info = getSessionInformation(sessionId);
		if (info != null && info.getPrincipal() instanceof WMUserDetails) {
			for (PlayerStateListener listener : listeners) {
				listener.playerAlive(((WMUserDetails) info.getPrincipal()).getPlayer());
			}
		}
	}

	@Override
	public void removeSessionInformation(String sessionId) {
		final SessionInformation info = getSessionInformation(sessionId);

		super.removeSessionInformation(sessionId);

		if (info != null && info.getPrincipal() instanceof WMUserDetails) {
			final Player player = ((WMUserDetails) info.getPrincipal()).getPlayer();
			// notify listeners only about last session
			if (getAllSessions(player, true).isEmpty()) {
				for (PlayerStateListener listener : listeners) {
					listener.playerOffline(((WMUserDetails) info.getPrincipal()).getPlayer());
				}
			}
		}
	}
}
