package wisematches.playground.tournament.impl.tournament;

import org.hibernate.SessionFactory;
import wisematches.database.Orders;
import wisematches.database.Range;
import wisematches.personality.Personality;
import wisematches.playground.search.SearchFilter;
import wisematches.playground.tournament.*;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTournamentManager implements TournamentManager<TournamentEntity<TournamentEntityId>, TournamentEntityId> {
	private SessionFactory sessionFactory;

	public HibernateTournamentManager() {
	}

	@Override
	public void addTournamentListener(TournamentListener l) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void removeTournamentListener(TournamentListener l) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Tournament getTournament(int number) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public TournamentRound getTournamentRound(TournamentRoundId roundId) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public TournamentGroup getTournamentGroup(TournamentGroupId groupId) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public int getTotalCount(Personality person, TournamentEntityId context) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public int getFilteredCount(Personality person, TournamentEntityId context, SearchFilter filter) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public List<TournamentEntity<TournamentEntityId>> searchEntities(Personality person, TournamentEntityId context, SearchFilter filter, Orders orders, Range range) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}