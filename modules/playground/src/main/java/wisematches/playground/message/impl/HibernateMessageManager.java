package wisematches.playground.message.impl;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.personality.player.PlayerManager;
import wisematches.playground.GameBoard;
import wisematches.playground.message.Message;
import wisematches.playground.message.MessageListener;
import wisematches.playground.message.MessageManager;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateMessageManager extends HibernateDaoSupport implements MessageManager {
    private PlayerManager playerManager;

    private final Collection<MessageListener> listeners = new CopyOnWriteArraySet<MessageListener>();

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
    @SuppressWarnings("unchecked")
    public Collection<Message> getMessages(Personality person) {
        final HibernateTemplate template = getHibernateTemplate();
        return template.find("from wisematches.playground.message.Message where recipient = ?", person.getId());
    }

    @Override
    public void removeMessage(long messageId) {
        final HibernateTemplate template = getHibernateTemplate();
        template.bulkUpdate("delete from wisematches.playground.message.Message where id= ?", messageId);
    }

    @Override
    public void removeMessages(Personality sender, Personality recipient) {
        final HibernateTemplate template = getHibernateTemplate();
        template.bulkUpdate("delete from wisematches.playground.message.Message where recipient= ? and sender = ?",
                recipient.getId(), sender.getId());
    }

    @Override
    public void clearMessages(Personality person) {
        final HibernateTemplate template = getHibernateTemplate();
        template.bulkUpdate("delete from wisematches.playground.message.Message where recipient = ?", person.getId());
    }

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }
}
