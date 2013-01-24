package wisematches.server.services.message.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Personality;
import wisematches.core.personality.player.MemberPlayerManager;
import wisematches.core.Membership;
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
	private MemberPlayerManager playerManager;
	private SessionFactory sessionFactory;

	private RestrictionManager restrictionManager;

	private final Lock removesLock = new ReentrantLock();

	private final Collection<MessageListener> listeners = new CopyOnWriteArraySet<>();
	private static final Log log = LogFactory.getLog("wisematches.server.playground.message");

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
	public void sendNotification(Personality recipient, String body) {
		sendNotification(recipient, body, false);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void sendMessage(Personality sender, Personality recipient, String body) {
		sendMessage(sender, recipient, body, false);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void replyMessage(Personality sender, Message message, String body) {
		replyMessage(sender, message, body, false);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void sendMessage(Personality sender, Personality recipient, String body, boolean quite) {
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
	public void replyMessage(Personality sender, Message message, String body, boolean quite) {
		if (sender == null) {
			throw new NullPointerException("Sender can't be null");
		}
		if (message == null) {
			throw new NullPointerException("Message can't be null");
		}

		final Personality recipient = playerManager.getPlayer(message.getSender());
		if (recipient == null) {
			throw new IllegalStateException("Recipient is unknown");
		}
		publishMessage(new HibernateMessage(message, body, sender), quite);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void sendNotification(Personality recipient, String body, boolean quite) {
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
	public int getNewMessagesCount(final Personality person) {
		final Session session = sessionFactory.getCurrentSession();
		final SQLQuery sqlQuery = session.createSQLQuery("select count(*) " +
				"FROM player_message as m left join player_activity as a on m.recipient=a.pid " +
				"WHERE m.recipient=:pid and (a.last_messages_check is null or m.created>a.last_messages_check)");
		sqlQuery.setLong("pid", person.getId());
		return ((Number) sqlQuery.uniqueResult()).intValue();
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getTodayMessagesCount(Personality person, MessageDirection direction) {
		final Session session = sessionFactory.getCurrentSession();
		final Query query;
		if (direction == MessageDirection.SENT) {
			query = session.createQuery("select count(*) " +
					"from wisematches.playground.message.impl.HibernateMessage " +
					"where sender = :pid and creationDate>= CURDATE()");
		} else {
			query = session.createQuery("select count(*) " +
					"from wisematches.playground.message.impl.HibernateMessage " +
					"where recipient = :pid and creationDate>= CURDATE()");
		}
		query.setLong("pid", person.getId());
		return ((Number) query.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Collection<Message> getMessages(Personality person, MessageDirection direction) {
		if (direction == MessageDirection.RECEIVED) {
			updateLastCheckTime(person);
		}

		final Session session = sessionFactory.getCurrentSession();
		final Query query;
		if (direction == MessageDirection.SENT) {
			query = session.createQuery("from wisematches.playground.message.impl.HibernateMessage where " +
					"sender = :pid and state in (0, :state) order by creationDate desc");
			query.setLong("pid", person.getId());
			query.setInteger("state", MessageDirection.RECEIVED.mask());
		} else {
			query = session.createQuery("from wisematches.playground.message.impl.HibernateMessage where " +
					"recipient = :pid and state in (0,:state) order by creationDate desc");
			query.setLong("pid", person.getId());
			query.setInteger("state", MessageDirection.SENT.mask());
		}
		return query.list();
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void removeMessage(Personality person, long messageId, MessageDirection direction) {
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
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void cleanup() {
		final StringBuilder b = new StringBuilder();
		b.append("DELETE m FROM player_message as m INNER JOIN account_personality as a ON a.id=m.recipient where ");
		b.append("(");
		b.append("(state = 3 and created < DATE_SUB(curdate(), INTERVAL 1 DAY))");

		final Membership[] values = Membership.values();
		if (restrictionManager.containsRestriction("messages.hist.private")) {
			b.append(" or ");
			b.append("(m.notification and ");
			for (Membership value : values) {
				Comparable restriction = restrictionManager.getRestrictionThreshold("messages.hist.private", value);
				if (restriction != null) {
					b.append("(a.membership = '").append(value.name()).append("' and created < DATE_SUB(curdate(), INTERVAL ").append(restriction).append(" DAY)) or ");
				}
			}
			b.setLength(b.length() - 4);
			b.append(")");
		}

		if (restrictionManager.containsRestriction("messages.hist.notice")) {
			b.append(" or ");
			b.append("(not m.notification and ");
			for (Membership value : values) {
				Comparable restriction = restrictionManager.getRestrictionThreshold("messages.hist.notice", value);
				if (restriction != null) {
					b.append("(a.membership = '").append(value.name()).append("' and created < DATE_SUB(curdate(), INTERVAL ").append(restriction).append(" DAY)) or ");
				}
			}
			b.setLength(b.length() - 4);
			b.append(")");
		}
		b.append(")");

		log.info("Cleanup old messages: " + b);

		removesLock.lock();
		try {
			sessionFactory.getCurrentSession().createSQLQuery(b.toString()).executeUpdate();
		} finally {
			removesLock.unlock();
		}
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setPlayerManager(MemberPlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public void setRestrictionManager(RestrictionManager restrictionManager) {
		this.restrictionManager = restrictionManager;
	}
}
