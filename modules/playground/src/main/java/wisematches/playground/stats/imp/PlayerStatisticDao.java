package wisematches.playground.stats.imp;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.playground.tracking.StatisticsEditor;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerStatisticDao extends HibernateDaoSupport {
	PlayerStatisticDao() {
	}

	@Transactional(propagation = Propagation.MANDATORY)
	<T extends StatisticsEditor> T loadPlayerStatistic(Class<? extends T> clazz, Personality personality) {
		return getHibernateTemplate().get(clazz, personality.getId());
	}

	@Transactional(propagation = Propagation.MANDATORY)
	void savePlayerStatistic(StatisticsEditor statistic) {
		getHibernateTemplate().saveOrUpdate(statistic);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	void removePlayerStatistic(StatisticsEditor statistic) {
		getHibernateTemplate().delete(statistic);
	}
}
