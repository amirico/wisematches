package wisematches.server.web.services.notify.impl.publisher;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Personality;
import wisematches.core.personality.member.account.Account;
import wisematches.server.web.services.notify.Notification;
import wisematches.server.web.services.notify.NotificationException;
import wisematches.server.web.services.notify.NotificationScope;
import wisematches.server.web.services.notify.impl.NotificationPublisher;
import wisematches.server.web.services.state.PlayerStateManager;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class EssentialNotificationPublisher implements NotificationPublisher {
	private SessionFactory sessionFactory;

	private PlayerStateManager playerStateManager;
	private NotificationPublisher notificationPublisher;

	private final Set<String> redundantNotifications = new HashSet<>();

	public EssentialNotificationPublisher() {
	}

	@Override
	public String getName() {
		return notificationPublisher.getName() + "-essential";
	}

	@Override
	public NotificationScope getNotificationScope() {
		return notificationPublisher.getNotificationScope();
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY, readOnly = false)
	public void publishNotification(Notification notification) throws NotificationException {
		final String code = notification.getCode();
		final Account target = notification.getTarget();

		if (redundantNotifications.contains(code)) {
			final Date activityDate = playerStateManager.getLastActivityDate(target);
			final Date notificationDate = getNotificationDate(target, code);
			if (notificationDate != null && (activityDate == null || !notificationDate.before(activityDate))) {
				return; // if redundant already sent - do nothing.
			}
			updateNotificationDate(target, code, notification.getTimestamp());
		}
		notificationPublisher.publishNotification(notification);
	}

	private Date getNotificationDate(Personality personality, String code) {
		final Session session = sessionFactory.getCurrentSession();
		final Query sqlQuery = session.createSQLQuery("SELECT `" + code + "` FROM notification_timestamp WHERE pid=:pid");
		sqlQuery.setLong("pid", personality.getId());
		return (Date) sqlQuery.uniqueResult();
	}

	private void updateNotificationDate(Personality personality, String code, long timestamp) {
		final Session session = sessionFactory.getCurrentSession();
		final String query = "INSERT INTO notification_timestamp (`pid`, `" + code + "`) VALUES (:pid, :time) " +
				"ON DUPLICATE KEY UPDATE `" + code + "`=:time";
		final Query sqlQuery = session.createSQLQuery(query);
		sqlQuery.setLong("pid", personality.getId());
		sqlQuery.setTimestamp("time", new Timestamp(timestamp));
		sqlQuery.executeUpdate();
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setPlayerStateManager(PlayerStateManager playerStateManager) {
		this.playerStateManager = playerStateManager;
	}

	public void setRedundantNotifications(Set<String> redundantNotifications) {
		this.redundantNotifications.clear();

		if (redundantNotifications != null) {
			this.redundantNotifications.addAll(redundantNotifications);
		}
	}

	public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
		this.notificationPublisher = notificationPublisher;
	}
}