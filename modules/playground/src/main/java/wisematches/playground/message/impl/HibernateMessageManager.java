package wisematches.playground.message.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
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

import java.sql.SQLException;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateMessageManager extends HibernateDaoSupport implements MessageManager {
    private PlayerManager playerManager;
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
        if (recipient == null) {
            throw new NullPointerException("Recipient can't be null");
        }
        publishMessage(new HibernateMessage(recipient, body));
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void sendMessage(Personality sender, Personality recipient, String body) {
        if (recipient == null) {
            throw new NullPointerException("Recipient can't be null");
        }
        if (sender == null) {
            throw new NullPointerException("Sender can't be null");
        }
        publishMessage(new HibernateMessage(recipient, body, sender));
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void replyMessage(Personality sender, Message message, String body) {
        if (sender == null) {
            throw new NullPointerException("Sender can't be null");
        }
        if (message == null) {
            throw new NullPointerException("Message can't be null");
        }

        final Player recipient = playerManager.getPlayer(message.getSender());
        publishMessage(new HibernateMessage(message, body, recipient));
    }

    private void publishMessage(Message m) {
        final HibernateTemplate template = getHibernateTemplate();
        template.save(m);

        for (MessageListener listener : listeners) {
            listener.messageSent(m);
        }
    }

    @Override
    public Message getMessage(long id) {
        return getHibernateTemplate().get(HibernateMessage.class, id);
    }

    @Override
    public int getNewMessagesCount(final Personality person) {
        return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
            @Override
            public Integer doInHibernate(Session session) throws HibernateException, SQLException {
                final SQLQuery sqlQuery = session.createSQLQuery("select count(*) " +
                        "FROM player_message as m left join player_activity as a on m.recipient=a.pid " +
                        "WHERE m.recipient=? and (a.last_messages_check is null or m.created>a.last_messages_check)");
                sqlQuery.setLong(0, person.getId());
                return ((Number) sqlQuery.uniqueResult()).intValue();
            }
        });
    }

    @Override
    public int getTodayMessagesCount(Personality person, MessageDirection direction) {
        final HibernateTemplate template = getHibernateTemplate();
        if (direction == MessageDirection.SENT) {
            return DataAccessUtils.intResult(template.find("select count(*) " +
                    "from wisematches.playground.message.impl.HibernateMessage " +
                    "where sender = ? and creationDate>= CURDATE()", person.getId()));
        } else {
            return DataAccessUtils.intResult(template.find("select count(*) " +
                    "from wisematches.playground.message.impl.HibernateMessage " +
                    "where recipient = ? and creationDate>= CURDATE()", person.getId()));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Message> getMessages(Personality person, MessageDirection direction) {
        final HibernateTemplate template = getHibernateTemplate();
        if (direction == MessageDirection.SENT) {
            return template.find("from wisematches.playground.message.impl.HibernateMessage where " +
                    "sender = ? and state in (0, ?)", person.getId(), MessageDirection.RECEIVED.mask());
        } else {
            return template.find("from wisematches.playground.message.impl.HibernateMessage where " +
                    "recipient = ? and state in (0,?)", person.getId(), MessageDirection.SENT.mask());
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void removeMessage(Personality person, long messageId, MessageDirection direction) {
        removesLock.lock();
        try {
            final HibernateTemplate template = getHibernateTemplate();

            final HibernateMessage message = template.get(HibernateMessage.class, messageId);
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
            template.save(message);
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
            getHibernateTemplate().execute(new HibernateCallback<Integer>() {
                @Override
                public Integer doInHibernate(Session session) throws HibernateException, SQLException {
                    SQLQuery sqlQuery = session.createSQLQuery(b.toString());
                    return sqlQuery.executeUpdate();
                }
            });
        } finally {
            removesLock.unlock();
        }
    }

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public void setRestrictionManager(RestrictionManager restrictionManager) {
        this.restrictionManager = restrictionManager;
    }
}
