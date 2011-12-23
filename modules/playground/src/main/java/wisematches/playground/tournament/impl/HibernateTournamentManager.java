package wisematches.playground.tournament.impl;

import org.hibernate.SessionFactory;
import wisematches.playground.tournament.TournamentManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTournamentManager implements TournamentManager {
	private SessionFactory sessionFactory;

	public HibernateTournamentManager() {
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
