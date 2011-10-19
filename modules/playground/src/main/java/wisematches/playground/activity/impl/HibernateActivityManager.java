package wisematches.playground.activity.impl;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.playground.activity.ActivityManager;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateActivityManager extends HibernateDaoSupport implements ActivityManager {
	public HibernateActivityManager() {
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void messagesChecked(Personality person) {
		HibernateTemplate template = getHibernateTemplate();
		LastMessagesCheck lastMessagesCheck = template.get(LastMessagesCheck.class, person.getId());
		if (lastMessagesCheck == null) {
			lastMessagesCheck = new LastMessagesCheck(person.getId(), new Date());
		} else {
			lastMessagesCheck.setLastCheckTime(new Date());
		}
		template.saveOrUpdate(lastMessagesCheck);
	}
}
