package wisematches.server.web.services.notify.impl.transform;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import wisematches.server.web.services.notify.Notification;
import wisematches.server.web.services.notify.NotificationMessage;
import wisematches.server.web.services.notify.NotificationTransformer;
import wisematches.server.web.services.notify.TransformationException;

/**
 * Implementation of {@code NotificationTransformer} that opens hibernate session for each transformation.
 * <p/>
 * The code and behaviour was taken from {@code org.springframework.orm.hibernate4.support.OpenSessionInViewFilter}.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class OpenSessionNotificationTransformer implements NotificationTransformer {
	private SessionFactory sessionFactory;
	private NotificationTransformer notificationTransformer;

	private static final Log log = LogFactory.getLog("wisematches.server.notify.transformer.hibernate");

	public OpenSessionNotificationTransformer() {
	}

	@Override
	public NotificationMessage createMessage(Notification notification) throws TransformationException {
		boolean participate = false;

		if (TransactionSynchronizationManager.hasResource(sessionFactory)) {
			// Do not modify the Session: just set the participate flag.
			participate = true;
		} else {
			log.debug("Opening Hibernate Session in OpenSessionInViewFilter");
			Session session = openSession(sessionFactory);
			TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
		}

		try {
			return notificationTransformer.createMessage(notification);
		} finally {
			if (!participate) {
				SessionHolder sessionHolder =
						(SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
				log.debug("Closing Hibernate Session in OpenSessionInViewFilter");
				SessionFactoryUtils.closeSession(sessionHolder.getSession());
			}
		}
	}

	private Session openSession(SessionFactory sessionFactory) throws DataAccessResourceFailureException {
		try {
			Session session = SessionFactoryUtils.openSession(sessionFactory);
			session.setFlushMode(FlushMode.MANUAL);
			return session;
		} catch (HibernateException ex) {
			throw new DataAccessResourceFailureException("Could not open Hibernate Session", ex);
		}
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setNotificationTransformer(NotificationTransformer notificationTransformer) {
		this.notificationTransformer = notificationTransformer;
	}
}
