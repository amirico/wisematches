package wisematches.server.standing.statistic.impl;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.server.personality.Personality;
import wisematches.server.standing.statistic.PlayerStatistic;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class PlayerStatisticDao extends HibernateDaoSupport {
	PlayerStatisticDao() {
	}

	@Transactional(propagation = Propagation.MANDATORY)
	PlayerStatistic loadPlayerStatistic(Personality personality) {
		return getHibernateTemplate().get(PlayerStatistic.class, personality.getId());
	}

	@Transactional(propagation = Propagation.MANDATORY)
	void savePlayerStatistic(Personality personality, PlayerStatistic statistic) {
		statistic.setUpdateTime(new Date());

		HibernateTemplate hibernateTemplate = getHibernateTemplate();
		hibernateTemplate.saveOrUpdate(statistic);
		hibernateTemplate.flush();
	}
}
