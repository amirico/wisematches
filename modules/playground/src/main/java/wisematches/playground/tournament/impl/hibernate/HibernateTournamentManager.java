package wisematches.playground.tournament.impl.hibernate;

import org.hibernate.SessionFactory;
import wisematches.playground.tournament.TournamentManager;
import wisematches.playground.tournament.impl.AbstractTournamentManager;

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
