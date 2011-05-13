package wisematches.tracking.stats.imp;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.tracking.stats.statistician.PlayerStatisticEditor;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class PlayerStatisticDao extends HibernateDaoSupport {
	PlayerStatisticDao() {
	}

	@Transactional(propagation = Propagation.MANDATORY)
	<T extends PlayerStatisticEditor> T loadPlayerStatistic(Class<? extends T> clazz, Personality personality) {
		return getHibernateTemplate().get(clazz, personality.getId());
	}

	@Transactional(propagation = Propagation.MANDATORY)
	void savePlayerStatistic(PlayerStatisticEditor statistic) {
//		statistic.setUpdateTime(new Date());
		getHibernateTemplate().saveOrUpdate(statistic);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	void removePlayerStatistic(PlayerStatisticEditor statistic) {
		getHibernateTemplate().delete(statistic);
	}
}
