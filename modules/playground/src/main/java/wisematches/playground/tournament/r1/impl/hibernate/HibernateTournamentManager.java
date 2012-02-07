package wisematches.playground.tournament.r1.impl.hibernate;

import org.hibernate.SessionFactory;
import wisematches.playground.tournament.r1.impl.AbstractTournamentManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTournamentManager extends AbstractTournamentManager {
	private SessionFactory sessionFactory;

	public HibernateTournamentManager() {
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
