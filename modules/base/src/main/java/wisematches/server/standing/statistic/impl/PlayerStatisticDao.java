package wisematches.server.standing.statistic.impl;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.server.personality.Personality;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class PlayerStatisticDao extends HibernateDaoSupport {
	PlayerStatisticDao() {
	}

	@Transactional(propagation = Propagation.MANDATORY)
	HibernatePlayerStatistic loadPlayerStatistic(Personality personality) {
		return getHibernateTemplate().get(HibernatePlayerStatistic.class, personality.getId());
	}

	@Transactional(propagation = Propagation.MANDATORY)
	void savePlayerStatistic(HibernatePlayerStatistic statistic) {
		statistic.setUpdateTime(new Date());

		HibernateTemplate hibernateTemplate = getHibernateTemplate();
		hibernateTemplate.saveOrUpdate(statistic);
		hibernateTemplate.flush();
	}

	@Transactional(propagation = Propagation.MANDATORY)
	void removePlayerStatistic(HibernatePlayerStatistic statistic) {
		HibernateTemplate hibernateTemplate = getHibernateTemplate();
		hibernateTemplate.delete(statistic);
		hibernateTemplate.flush();
	}
}
