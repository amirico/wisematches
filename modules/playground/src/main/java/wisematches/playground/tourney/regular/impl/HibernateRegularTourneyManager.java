package wisematches.playground.tourney.regular.impl;

import wisematches.database.Orders;
import wisematches.database.Range;
import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.playground.search.SearchFilter;
import wisematches.playground.tourney.TourneyEntity;
import wisematches.playground.tourney.TourneyEntityListener;
import wisematches.playground.tourney.regular.*;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateRegularTourneyManager implements RegularTourneyManager {
	private final Lock lock = new ReentrantLock();

	public HibernateRegularTourneyManager() {
	}

	@Override
	public void addRegularTourneyListener(RegularTourneyListener l) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public void removeRegularTourneyListener(RegularTourneyListener l) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public void addTourneySubscriptionListener(TourneySubscriptionListener l) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public void removeTourneySubscriptionListener(TourneySubscriptionListener l) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public void addTourneyEntityListener(TourneyEntityListener<? super RegularTourneyEntity> l) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public void removeTourneyEntityListener(TourneyEntityListener<? super RegularTourneyEntity> l) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}


	@Override
	public TourneySubscription subscribe(int tourney, long player, Language language, TourneySection section) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public TourneySubscription unsubscribe(int tourney, long player, Language language, TourneySection section) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public TourneySubscriptionStatus getSubscriptionStatus() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public TourneySubscription getSubscription(int tournament, long player) {
		lock.lock();
		try {
			return null;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public <T extends RegularTourneyEntity, K extends TourneyEntity.Id<? extends T, ?>> T getTournamentEntity(K id) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public <T extends RegularTourneyEntity, C extends TourneyEntity.Context<? extends T, ?>> List<T> searchTournamentEntities(Personality person, C context, SearchFilter filter, Orders orders, Range range) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public <Ctx extends TourneyEntity.Context<? extends RegularTourneyEntity, ?>> int getTotalCount(Personality person, Ctx context) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public <Ctx extends TourneyEntity.Context<? extends RegularTourneyEntity, ?>, Fl extends SearchFilter> int getFilteredCount(Personality person, Ctx context, Fl filter) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public <Ctx extends TourneyEntity.Context<? extends RegularTourneyEntity, ?>, Fl extends SearchFilter> List<RegularTourneyEntity> searchEntities(Personality person, Ctx context, Fl filter, Orders orders, Range range) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}
}
