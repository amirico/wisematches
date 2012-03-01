package wisematches.playground.tournament.active.impl;

import org.hibernate.SessionFactory;
import wisematches.personality.player.Player;
import wisematches.playground.search.SearchManager;
import wisematches.playground.search.descriptive.DescriptiveSearchManager;
import wisematches.playground.tournament.*;
import wisematches.playground.tournament.active.TournamentListener;
import wisematches.playground.tournament.active.TournamentManager;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTournamentManager implements TournamentManager {
	private SessionFactory sessionFactory;

	private final DescriptiveRoundSearchManager roundSearchManager = new DescriptiveRoundSearchManager();
	private final DescriptiveGroupSearchManager groupSearchManager = new DescriptiveGroupSearchManager();
	private final DescriptiveSectionSearchManager sectionSearchManager = new DescriptiveSectionSearchManager();

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
	public Collection<TournamentRound> getTournamentRounds(TournamentSectionId sectionId) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	public <E extends TournamentEntity<C>, C extends TournamentEntityId> SearchManager<E, C> getTournamentSearchManager(Class<E> type) {
/*
		if (type.isAssignableFrom(TournamentSection.class)) {
			return new DescriptiveSearchManagerWrapper<E, C>(sectionSearchManager);
		}
		if (type.isAssignableFrom(TournamentRound.class)) {
			return new DescriptiveSearchManagerWrapper<E, C>(roundSearchManager);
		}
		if (type.isAssignableFrom(TournamentGroup.class)) {
			return new DescriptiveSearchManagerWrapper<E, C>(groupSearchManager);
		}
*/
		throw new IllegalArgumentException("Unsupported tournament entity type: " + type);
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;

		roundSearchManager.setSessionFactory(sessionFactory);
		groupSearchManager.setSessionFactory(sessionFactory);
		sectionSearchManager.setSessionFactory(sessionFactory);
	}
}
