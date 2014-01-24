package wisematches.server.services.message.impl;


import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Membership;
import wisematches.core.Personality;
import wisematches.core.PersonalityManager;
import wisematches.core.Player;
import wisematches.playground.restriction.RestrictionManager;
import wisematches.server.services.message.Message;
import wisematches.server.services.message.MessageDirection;
import wisematches.server.services.message.MessageListener;
import wisematches.server.services.message.MessageManager;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateMessageManager implements MessageManager {
	private PersonalityManager playerManager;
	private SessionFactory sessionFactory;

	private RestrictionManager restrictionManager;

	private final Lock removesLock = new ReentrantLock();

	private final Collection<MessageListener> listeners = new CopyOnWriteArraySet<>();
	private static final Logger log = LoggerFactory.getLogger("wisematches.message.Manager");

	public HibernateMessageManager() {
	}

	@Override
	public void addMessageListener(MessageListener l) {
		if (l != null) {
			listeners.add(l);
		}
	}

	@Override
	public void removeMessageListener(MessageListener l) {
		listeners.remove(l);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void sendNotification(Player recipient, String body) {
		sendNotification(recipient, body, false);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void sendMessage(Player sender, Player recipient, String body) {
		sendMessage(sender, recipient, body, false);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void replyMessage(Player sender, Message message, String body) {
		replyMessage(sender, message, body, false);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void sendMessage(Player sender, Player recipient, String body, boolean quite) {
		if (recipient == null) {
			throw new NullPointerException("Recipient can't be null");
		}
		if (sender == null) {
			throw new NullPointerException("Sender can't be null");
		}
		publishMessage(new HibernateMessage(recipient, body, sender), quite);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void replyMessage(Player sender, Message message, String body, boolean quite) {
		if (sender == null) {
			throw new NullPointerException("Sender can't be null");
		}
		if (message == null) {
			throw new NullPointerException("Message can't be null");
		}

		final Personality recipient = playerManager.getMember(message.getSender());
		if (recipient == null) {
			throw new IllegalStateException("Recipient is unknown");
		}
		publishMessage(new HibernateMessage(message, body, sender), quite);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void sendNotification(Player recipient, String body, boolean quite) {
		if (recipient == null) {
			throw new NullPointerException("Recipient can't be null");
		}
		publishMessage(new HibernateMessage(recipient, body), quite);
	}

	private void publishMessage(Message m, boolean quite) {
		sessionFactory.getCurrentSession().save(m);

		for (MessageListener listener : listeners) {
			listener.messageSent(m, quite);
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Message getMessage(long id) {
		return (Message) sessionFactory.getCurrentSession().get(HibernateMessage.class, id);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public int getNewMessagesCount(final Player person) {
		final Session session = sessionFactory.getCurrentSession();
		final SQLQuery sqlQuery = session.createSQLQuery("SELECT count(*) " +
				"FROM player_message AS m LEFT JOIN player_activity AS a ON m.recipient=a.pid " +
				"WHERE m.recipient=:pid AND (a.last_messages_check IS NULL OR m.created>a.last_messages_check)");
		sqlQuery.setParameter("pid", person.getId());
		return ((Number) sqlQuery.uniqueResult()).intValue();
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getTodayMessagesCount(Player person, MessageDirection direction) {
		final Session session = sessionFactory.getCurrentSession();
		final Query query;
		if (direction == MessageDirection.SENT) {
			query = session.createQuery("select count(*) " +
					"from HibernateMessage " +
					"where sender = :pid and creationDate>= CURDATE()");
		} else {
			query = session.createQuery("select count(*) " +
					"from HibernateMessage " +
					"where recipient = :pid and creationDate>= CURDATE()");
		}
		query.setParameter("pid", person.getId());
		return ((Number) query.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Collection<Message> getMessages(Player person, MessageDirection direction) {
		if (direction == MessageDirection.RECEIVED) {
			updateLastCheckTime(person);
		}

		final Session session = sessionFactory.getCurrentSession();
		final Query query;
		if (direction == MessageDirection.SENT) {
			query = session.createQuery("from HibernateMessage where " +
					"sender = :pid and state in (0, :state) order by creationDate desc");
			query.setParameter("pid", person.getId());
			query.setInteger("state", MessageDirection.RECEIVED.mask());
		} else {
			query = session.createQuery("from HibernateMessage where " +
					"recipient = :pid and state in (0,:state) order by creationDate desc");
			query.setParameter("pid", person.getId());
			query.setInteger("state", MessageDirection.SENT.mask());
		}
		return query.list();
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void removeMessage(Player person, long messageId, MessageDirection direction) {
		removesLock.lock();
		try {
			final Session session = sessionFactory.getCurrentSession();

			final HibernateMessage message = (HibernateMessage) session.get(HibernateMessage.class, messageId);
			if (message == null) {
				return;
			}
			if (direction == MessageDirection.RECEIVED && message.getRecipient() != person.getId()) {
				throw new IllegalArgumentException("Specified player didn't receive the message");
			}
			if (direction == MessageDirection.SENT && message.getSender() != person.getId()) {
				throw new IllegalArgumentException("Specified player didn't send the message");
			}
			message.setState(direction.add(message.getState()));
			session.save(message);
		} finally {
			removesLock.unlock();
		}
	}

	private void updateLastCheckTime(Personality person) {
		final Session session = sessionFactory.getCurrentSession();
		HibernateMessageActivity hibernateMessageActivity = (HibernateMessageActivity) session.get(HibernateMessageActivity.class, person.getId());
		if (hibernateMessageActivity == null) {
			hibernateMessageActivity = new HibernateMessageActivity(person.getId(), new Date());
		} else {
			hibernateMessageActivity.setLastCheckTime(new Date());
		}
		session.saveOrUpdate(hibernateMessageActivity);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void cleanup(Date today) {
		int total = 0;
		final Session session = sessionFactory.getCurrentSession();
		removesLock.lock();
		try {
			final SQLQuery removeExpired = session.createSQLQuery("DELETE m FROM player_message AS m WHERE m.state=3 AND m.created < (:date-INTERVAL 1 DAY)");
			removeExpired.setDate("date", today);
			total += removeExpired.executeUpdate();

			final SQLQuery removeMessages = session.createSQLQuery(createCleanQuery(true));
			removeMessages.setDate("date", today);
			total += removeMessages.executeUpdate();

			final SQLQuery removeNotifications = session.createSQLQuery(createCleanQuery(false));
			removeNotifications.setDate("date", today);
			total += removeNotifications.executeUpdate();
		} finally {
			removesLock.unlock();
		}
		log.info("Cleanup old messages: {}", total);
	}

	private String createCleanQuery(boolean message) {
		final String name = message ? "messages.hist.private" : "messages.hist.notice";

		if (restrictionManager.containsRestriction(name)) {
			final StringBuilder b = new StringBuilder();
			b.append("DELETE m FROM player_message as m LEFT JOIN account_membership as a ON a.pid=m.recipient where ");

			b.append(message ? "m.sender!=0" : "m.sender=0"); // sender=0 - notification
			b.append(" and (");
			Comparable restriction = restrictionManager.getRestrictionThreshold(name, Membership.BASIC);
			if (restriction != null) {
				b.append("(a.membership is null and created < DATE_SUB(:date, INTERVAL ").append(restriction).append(" DAY)) or ");
			}
			for (Membership value : Membership.values()) {
				restriction = restrictionManager.getRestrictionThreshold(name, value);
				if (restriction != null) {
					b.append("(a.membership = ").append(value.ordinal()).append(" and created < DATE_SUB(:date, INTERVAL ").append(restriction).append(" DAY)) or ");
				}
			}
			b.setLength(b.length() - 4);
			b.append(")");
			return b.toString();
		}
		return "select 1";
	}

	public void setPersonalityManager(PersonalityManager playerManager) {
		this.playerManager = playerManager;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setRestrictionManager(RestrictionManager restrictionManager) {
		this.restrictionManager = restrictionManager;
	}
}
