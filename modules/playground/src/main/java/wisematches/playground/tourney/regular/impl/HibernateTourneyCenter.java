package wisematches.playground.tourney.regular.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.InitializingBean;
import wisematches.playground.scheduling.BreakingDayListener;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTourneyCenter implements InitializingBean, BreakingDayListener {
	private SessionFactory sessionFactory;

	//	private RegularTourneyManager tourneyManager;
	public HibernateTourneyCenter() {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO: check already sent message
	}

	@Override
	public void breakingDayTime(Date midnight) {
		final Session currentSession = sessionFactory.getCurrentSession();
		final Query query = currentSession.createQuery("");

//		final List<Tourney> tourneys = tourneyManager.searchTourneyEntities(null, new Tourney.Context(EnumSet.of(TourneyEntity.State.SCHEDULED)), null, null, null);
//		for (Tourney tourney : tourneys) {
//
//		}
	}
/*

	public void setTourneyManager(RegularTourneyManager tourneyManager) {
		this.tourneyManager = tourneyManager;
	}
*/

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
