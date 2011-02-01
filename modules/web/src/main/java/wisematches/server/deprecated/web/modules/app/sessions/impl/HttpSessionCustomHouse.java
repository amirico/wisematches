package wisematches.server.deprecated.web.modules.app.sessions.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.server.deprecated.web.server.sessions.WebSessionCustomHouse;
import wisematches.server.player.Player;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * This {@code PlayerCustomHouse} listen HTTP sessions and fires {@code playerMoveOut} event
 * when session is destroyed.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class HttpSessionCustomHouse implements HttpSessionListener {
	private WebSessionCustomHouse sessionCustomHouse;

	private static final Log log = LogFactory.getLog(HttpSessionCustomHouse.class);

	public HttpSessionCustomHouse() {
	}

	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		if (log.isDebugEnabled()) {
			log.debug("Session is created: " + httpSessionEvent.getSession().getId());
		}
	}

	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		final HttpSession session = httpSessionEvent.getSession();
		if (log.isDebugEnabled()) {
			log.debug("Session is destroyed: " + httpSessionEvent.getSession().getId());
		}

		final Player player = sessionCustomHouse.getLoggedInPlayer(session);
		if (player != null) {
			sessionCustomHouse.performLogout(player, session);
		}
	}

	public void setSessionCustomHouse(WebSessionCustomHouse sessionCustomHouse) {
		this.sessionCustomHouse = sessionCustomHouse;
	}
}