package wisematches.server.services.state.impl;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistryImpl;
import wisematches.core.Personality;
import wisematches.core.security.userdetails.PersonalityDetails;
import wisematches.server.services.state.PlayerStateListener;
import wisematches.server.services.state.PlayerStateManager;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SessionRegistryStateManager extends SessionRegistryImpl implements PlayerStateManager {
	private final Collection<PlayerStateListener> listeners = new CopyOnWriteArraySet<>();

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
		if (info != null && info.getPrincipal() instanceof PersonalityDetails) {
			processPlayerOnline(personality(info));
		}
	}

	@Override
	public void refreshLastRequest(String sessionId) {
		super.refreshLastRequest(sessionId);

		final SessionInformation info = getSessionInformation(sessionId);
		if (info != null && info.getPrincipal() instanceof PersonalityDetails) {
			processPlayerAlive(personality(info));
		}
	}

	@Override
	public void removeSessionInformation(String sessionId) {
		final SessionInformation info = getSessionInformation(sessionId);

		super.removeSessionInformation(sessionId);

		if (info != null && info.getPrincipal() instanceof PersonalityDetails) {
			final Personality player = personality(info);
			// notify listeners only about last session
			if (getAllSessions(player, true).isEmpty()) {
				processPlayerOffline(personality(info));
			}
		}
	}

	private Personality personality(SessionInformation info) {
		return ((PersonalityDetails) info.getPrincipal()).getPersonality();
	}

	protected void processPlayerOnline(Personality player) {
		// notify listeners only about first session
		if (getAllSessions(player, true).size() == 1) {
			for (PlayerStateListener listener : listeners) {
				listener.playerOnline(player);
			}
		}
	}

	protected void processPlayerAlive(Personality player) {
		for (PlayerStateListener listener : listeners) {
			listener.playerAlive(player);
		}
	}

	protected void processPlayerOffline(Personality player) {
		for (PlayerStateListener listener : listeners) {
			listener.playerOffline(player);
		}
	}
}
