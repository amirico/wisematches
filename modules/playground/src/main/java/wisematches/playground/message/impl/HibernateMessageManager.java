package wisematches.playground.message.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Membership;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.personality.player.PlayerManager;
import wisematches.playground.message.Message;
import wisematches.playground.message.MessageDirection;
import wisematches.playground.message.MessageListener;
import wisematches.playground.message.MessageManager;
import wisematches.playground.restriction.RestrictionDescription;
import wisematches.playground.restriction.RestrictionManager;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateMessageManager implements MessageManager {
	private PlayerManager playerManager;
	private SessionFactory sessionFactory;

	private RestrictionManager restrictionManager;

	private final Lock removesLock = new ReentrantLock();

	private final Collection<MessageListener> listeners = new CopyOnWriteArraySet<MessageListener>();
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

		final Player recipient = playerManager.getPlayer(message.getSender());
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
				"WHERE m.recipient=? and (a.last_messages_check is null or m.created>a.last_messages_check)");
		sqlQuery.setLong(0, person.getId());
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
					"where sender = ? and creationDate>= CURDATE()");
		} else {
			query = session.createQuery("select count(*) " +
					"from wisematches.playground.message.impl.HibernateMessage " +
					"where recipient = ? and creationDate>= CURDATE()");
		}
		query.setParameter(0, person.getId());
		return ((Number) query.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Collection<Message> getMessages(Personality person, MessageDirection direction) {
		final Session session = sessionFactory.getCurrentSession();
		final Query query;
		if (direction == MessageDirection.SENT) {
			query = session.createQuery("from wisematches.playground.message.impl.HibernateMessage where " +
					"sender = ? and state in (0, ?) order by creationDate desc");
			query.setParameter(0, person.getId());
			query.setParameter(1, MessageDirection.RECEIVED.mask());
		} else {
			query = session.createQuery("from wisematches.playground.message.impl.HibernateMessage where " +
					"recipient = ? and state in (0,?) order by creationDate desc");
			query.setParameter(0, person.getId());
			query.setParameter(1, MessageDirection.SENT.mask());
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

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void cleanup() {
		RestrictionDescription msgHist = null;
		RestrictionDescription noticeHist = null;
		final Collection<RestrictionDescription> descriptions = restrictionManager.getRestrictionDescriptions();
		for (RestrictionDescription description : descriptions) {
			if ("messages.hist.private".equals(description.getName())) {
				msgHist = description;
			} else if ("messages.hist.notice".equals(description.getName())) {
				noticeHist = description;
			}
		}

		Membership[] values = Membership.values();
		final StringBuilder b = new StringBuilder();
		b.append("DELETE m FROM player_message as m INNER JOIN account_personality as a ON a.id=m.recipient where ");
		b.append("(");
		b.append("(state = 3 and created < DATE_SUB(curdate(), INTERVAL 1 DAY))");
		if (msgHist != null) {
			b.append(" or ");
			b.append("(m.notification and ");
			for (Membership value : values) {
				Comparable restriction = msgHist.getRestriction(value);
				if (restriction != null) {
					b.append("(a.membership = '").append(value.name()).append("' and created < DATE_SUB(curdate(), INTERVAL ").append(restriction).append(" DAY)) or ");
				}
			}
			b.setLength(b.length() - 4);
			b.append(")");
		}
		if (noticeHist != null) {
			if (msgHist != null) {
				b.append(" or ");
			}
			b.append("(not m.notification and ");
			for (Membership value : values) {
				Comparable restriction = noticeHist.getRestriction(value);
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

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public void setRestrictionManager(RestrictionManager restrictionManager) {
		this.restrictionManager = restrictionManager;
	}
}
