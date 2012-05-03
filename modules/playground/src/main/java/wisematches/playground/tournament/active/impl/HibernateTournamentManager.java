package wisematches.playground.tournament.active.impl;

import org.hibernate.SessionFactory;
import wisematches.database.Orders;
import wisematches.database.Range;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.playground.search.SearchFilter;
import wisematches.playground.tournament.*;
import wisematches.playground.tournament.active.TournamentListener;
import wisematches.playground.tournament.active.TournamentManager;

import java.util.Collection;
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
	public Collection<Tournament> getActiveTournaments() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Collection<Tournament> getPlayerTournaments(Player player) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public int getTotalCount(Personality person, TournamentEntityId context) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public int getFilteredCount(Personality person, TournamentEntityId context, SearchFilter filter) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public List<TournamentEntity<TournamentEntityId>> searchEntities(Personality person, TournamentEntityId context, SearchFilter filter, Orders orders, Range range) {

		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}