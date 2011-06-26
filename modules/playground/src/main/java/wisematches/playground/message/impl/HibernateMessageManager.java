package wisematches.playground.message.impl;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import wisematches.personality.Personality;
import wisematches.playground.message.Message;
import wisematches.playground.message.MessageListener;
import wisematches.playground.message.MessageManager;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateMessageManager extends HibernateDaoSupport implements MessageManager {
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
    public void sendMessage(Personality person, String subject, String body) {
        final Message m = new Message(person, new Date(), subject, body);

        final HibernateTemplate template = getHibernateTemplate();
        template.save(m);

        for (MessageListener listener : listeners) {
            listener.messageSent(m);
        }
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
    public void clearMessages(Personality person) {
        final HibernateTemplate template = getHibernateTemplate();
        template.bulkUpdate("delete from wisematches.playground.message.Message where recipient = ?", person.getId());
    }
}
