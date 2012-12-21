package wisematches.playground.achievement.impl;

import org.hibernate.SessionFactory;
import wisematches.playground.tourney.regular.RegularTourneyListener;
import wisematches.playground.tourney.regular.RegularTourneyManager;
import wisematches.playground.tourney.regular.Tourney;
import wisematches.playground.tourney.regular.TourneyDivision;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateAchievementCenter {
	private SessionFactory sessionFactory;
	private RegularTourneyManager tourneyManager;

	public HibernateAchievementCenter() {
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setTourneyManager(RegularTourneyManager tourneyManager) {
		this.tourneyManager = tourneyManager;

		this.tourneyManager.addRegularTourneyListener(new RegularTourneyListener() {
			@Override
			public void tourneyAnnounced(Tourney tourney) {
			}

			@Override
			public void tourneyStarted(Tourney tourney) {
			}

			@Override
			public void tourneyFinished(Tourney tourney, TourneyDivision division) {
			}
		});
	}
}
