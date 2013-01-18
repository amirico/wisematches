package wisematches.server.web.services.state.impl;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistryImpl;
import wisematches.core.Personality;
import wisematches.core.personality.Player;
import wisematches.server.security.impl.WMPlayerDetails;
import wisematches.server.web.services.state.PlayerStateListener;
import wisematches.server.web.services.state.PlayerStateManager;

import java.util.Collection;
import java.util.Date;
import java.util.List;
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
	public Date getLastActivityDate(Personality personality) {
		Date date = null;
		final List<SessionInformation> allSessions = getAllSessions(personality, true);
		for (SessionInformation session : allSessions) {
			final Date lastRequest = session.getLastRequest();
			if (date == null) {
				date = lastRequest;
			} else if (date.before(lastRequest)) {
				date = lastRequest;
			}
		}
		return date;
	}

	@Override
	public void registerNewSession(String sessionId, Object principal) {
		super.registerNewSession(sessionId, principal);

		final SessionInformation info = getSessionInformation(sessionId);
		if (info != null && info.getPrincipal() instanceof WMPlayerDetails) {
			processPlayerOnline(player(info));
		}
	}

	@Override
	public void refreshLastRequest(String sessionId) {
		super.refreshLastRequest(sessionId);

		final SessionInformation info = getSessionInformation(sessionId);
		if (info != null && info.getPrincipal() instanceof WMPlayerDetails) {
			processPlayerAlive(player(info));
		}
	}

	@Override
	public void removeSessionInformation(String sessionId) {
		final SessionInformation info = getSessionInformation(sessionId);

		super.removeSessionInformation(sessionId);

		if (info != null && info.getPrincipal() instanceof WMPlayerDetails) {
			final Player player = player(info);
			// notify listeners only about last session
			if (getAllSessions(player, true).isEmpty()) {
				processPlayerOffline(player(info));
			}
		}
	}

	private Player player(SessionInformation info) {
		return ((WMPlayerDetails) info.getPrincipal()).getPlayer();
	}

	protected void processPlayerOnline(Player player) {
		// notify listeners only about first session
		if (getAllSessions(player, true).size() == 1) {
			for (PlayerStateListener listener : listeners) {
				listener.playerOnline(player);
			}
		}
	}

	protected void processPlayerAlive(Player player) {
		for (PlayerStateListener listener : listeners) {
			listener.playerAlive(player);
		}
	}

	protected void processPlayerOffline(Player player) {
		for (PlayerStateListener listener : listeners) {
			listener.playerOffline(player);
		}
	}
}
