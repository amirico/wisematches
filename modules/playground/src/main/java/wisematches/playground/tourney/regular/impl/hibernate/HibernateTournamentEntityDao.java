package wisematches.playground.tourney.regular.impl.hibernate;

import org.hibernate.SessionFactory;
import wisematches.playground.tourney.TourneyEntity;
import wisematches.playground.tourney.regular.impl.TournamentEntityDao;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTournamentEntityDao implements TournamentEntityDao {
	private SessionFactory sessionFactory;

	public HibernateTournamentEntityDao() {
	}

	@Override
	public void markTournamentEntityProcessed(TourneyEntity entity) {
	}

	@Override
	public <T extends TourneyEntity> Collection<T> getUnprocessedTournamentEntities(Class<T> type) {
		throw new UnsupportedOperationException("Not implemented");
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
