package wisematches.playground.activity.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.playground.activity.ActivityManager;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateActivityManager implements ActivityManager {
	private SessionFactory sessionFactory;

	public HibernateActivityManager() {
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void messagesChecked(Personality person) {
		final Session session = sessionFactory.getCurrentSession();
		LastMessagesCheck lastMessagesCheck = (LastMessagesCheck) session.get(LastMessagesCheck.class, person.getId());
		if (lastMessagesCheck == null) {
			lastMessagesCheck = new LastMessagesCheck(person.getId(), new Date());
		} else {
			lastMessagesCheck.setLastCheckTime(new Date());
		}
		session.saveOrUpdate(lastMessagesCheck);
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
