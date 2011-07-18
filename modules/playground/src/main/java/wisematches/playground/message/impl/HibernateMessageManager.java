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
import wisematches.playground.GameBoard;
import wisematches.playground.message.Message;
import wisematches.playground.message.MessageListener;
import wisematches.playground.message.MessageManager;
import wisematches.playground.restriction.RestrictionDescription;
import wisematches.playground.restriction.RestrictionManager;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateMessageManager extends HibernateDaoSupport implements MessageManager {
    private PlayerManager playerManager;
    private RestrictionManager restrictionManager;

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
        publishMessage(new Message(recipient, body));
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
        publishMessage(new Message(recipient, body, sender));
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void replayMessage(Personality sender, Message message, String body) {
        if (sender == null) {
            throw new NullPointerException("Sender can't be null");
        }
        if (message == null) {
            throw new NullPointerException("Message can't be null");
        }

        final Player recipient = playerManager.getPlayer(message.getRecipient());
        publishMessage(new Message(recipient, body, sender, message));
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void sendMessage(Personality sender, Personality recipient, String body, GameBoard board) {
        if (recipient == null) {
            throw new NullPointerException("Recipient can't be null");
        }
        if (sender == null) {
            throw new NullPointerException("Sender can't be null");
        }
        if (board == null) {
            throw new NullPointerException("Board can't be null");
        }
        publishMessage(new Message(recipient, body, sender, board));
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
        return getHibernateTemplate().get(Message.class, id);
    }

    @Override
    public int getNewMessagesCount(final Personality person) {
        return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
            @Override
            public Integer doInHibernate(Session session) throws HibernateException, SQLException {
                final SQLQuery sqlQuery = session.createSQLQuery("select count(*) " +
                        "FROM player_message as m left join player_activity as a on m.recipient=a.pid " +
                        "WHERE m.recipient=? and (a.last_messages_check is null or m.created>=a.last_messages_check)");
                sqlQuery.setLong(0, person.getId());
                return ((Number) sqlQuery.uniqueResult()).intValue();
            }
        });
    }

    @Override
    public int getTodayMessagesCount(Personality person) {
        final HibernateTemplate template = getHibernateTemplate();
        return DataAccessUtils.intResult(template.find("select count(*) " +
                "from wisematches.playground.message.Message where sender = ? and created>= CURDATE()", person.getId()));
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.MANDATORY)
    public Collection<Message> getMessages(Personality person) {
        final HibernateTemplate template = getHibernateTemplate();

        LastMessagesCheck lastMessagesCheck = template.get(LastMessagesCheck.class, person.getId());
        if (lastMessagesCheck == null) {
            lastMessagesCheck = new LastMessagesCheck(person.getId(), new Date());
        } else {
            lastMessagesCheck.setLastCheckTime(new Date());
        }
        template.saveOrUpdate(lastMessagesCheck);

        return template.find("from wisematches.playground.message.Message where recipient = ?", person.getId());
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void removeMessage(long messageId) {
        final HibernateTemplate template = getHibernateTemplate();
        template.bulkUpdate("delete from wisematches.playground.message.Message where id= ?", messageId);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void removeMessages(Personality sender, Personality recipient) {
        final HibernateTemplate template = getHibernateTemplate();
        template.bulkUpdate("delete from wisematches.playground.message.Message where recipient= ? and sender = ?",
                recipient.getId(), sender.getId());
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void clearMessages(Personality person) {
        final HibernateTemplate template = getHibernateTemplate();
        template.bulkUpdate("delete from wisematches.playground.message.Message where recipient = ?", person.getId());
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
        b.append("DELETE m FROM player_message as m INNER JOIN account_personality as a ON a.id=m.recipient and ");
        b.append("(");
        if (msgHist != null) {
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

        getHibernateTemplate().execute(new HibernateCallback<Integer>() {
            @Override
            public Integer doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery(b.toString());
                return sqlQuery.executeUpdate();
            }
        });
    }

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public void setRestrictionManager(RestrictionManager restrictionManager) {
        this.restrictionManager = restrictionManager;
    }
}
