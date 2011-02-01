package wisematches.server.deprecated.web.sessions;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Spring configuration bean that allows add {@code HttpSessionListener} to it. This bean can be declared in
 * configuration file no more than once because it's uses static reference to {@code DispatcherSessionListener}
 * object.
 * <p/>
 * This bean registers itself in {@code DispatcherSessionListener} object after creation and removes itself
 * when removed. When bean is created it just retranslates all session events to all registred listeners.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class SessionNotificationBean implements InitializingBean, DisposableBean, HttpSessionListener {
	private final Collection<HttpSessionListener> listeners = new CopyOnWriteArraySet<HttpSessionListener>();

	public void afterPropertiesSet() throws Exception {
		DispatcherSessionListener.setSessionListenerMapping(this);
	}

	public void destroy() throws Exception {
		DispatcherSessionListener.setSessionListenerMapping(null);
	}

	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		for (HttpSessionListener listener : listeners) {
			listener.sessionCreated(httpSessionEvent);
		}
	}

	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		for (HttpSessionListener listener : listeners) {
			listener.sessionDestroyed(httpSessionEvent);
		}
	}

	public void setHttpSessionListeners(Collection<HttpSessionListener> listeners) {
		this.listeners.clear();
		this.listeners.addAll(listeners);
	}
}