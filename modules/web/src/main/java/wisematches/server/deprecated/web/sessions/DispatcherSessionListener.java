package wisematches.server.deprecated.web.sessions;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * This is static implementation of {@code HttpSessionListener} that is declared
 * in {@code web.xml}. This implementation has reference to {@code SessionNotificationBean}
 * object and if it is not {@code null} retranslates all received events into notification bean.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class DispatcherSessionListener implements HttpSessionListener {
	private static SessionNotificationBean sessionListenerMapping;

	public void sessionCreated(HttpSessionEvent event) {
		if (sessionListenerMapping != null) {
			sessionListenerMapping.sessionCreated(event);
		}
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		if (sessionListenerMapping != null) {
			sessionListenerMapping.sessionDestroyed(event);
		}
	}

	static void setSessionListenerMapping(SessionNotificationBean sessionListenerMapping) {
		DispatcherSessionListener.sessionListenerMapping = sessionListenerMapping;
	}
}
