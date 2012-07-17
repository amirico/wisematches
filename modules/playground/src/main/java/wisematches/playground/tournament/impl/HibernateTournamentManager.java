package wisematches.playground.tournament.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import wisematches.database.Orders;
import wisematches.database.Range;
import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.playground.search.SearchFilter;
import wisematches.playground.tournament.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTournamentManager extends AbstractTournamentManager {
	private SessionFactory sessionFactory;

	public HibernateTournamentManager() {
	}

	@Override
	protected AnnouncementImpl loadAnnouncement() {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria criteria = session.createCriteria(HibernateTournament.class);
		criteria.add(Restrictions.isNull("startedDate")).add(Restrictions.isNull("finishedDate"));
		final Tournament tournament = (Tournament) criteria.uniqueResult();
		if (tournament != null) {
			final AnnouncementImpl res = new AnnouncementImpl(tournament);
			final Criteria values = session.createCriteria(HibernateTournamentSubscription.class, "request")
					.add(Restrictions.eq("pk.announcement", res.getNumber()))
					.setProjection(Projections.projectionList()
							.add(Projections.groupProperty("pk.language"))
							.add(Projections.groupProperty("section"))
							.add(Projections.rowCount()));

			final List list = values.list();
			for (Object o : list) {
				final Object[] row = (Object[]) o;
				final Language l = (Language) row[0];
				final TournamentSection s = (TournamentSection) row[1];
				final Number c = (Number) row[2];
				res.setBoughtTickets(l, s, c.intValue());
			}
		}
		return null;
	}

	@Override
	protected Collection<TournamentSubscription> loadSubscriptions(int tournament, Player player) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	protected Tournament scheduleTournament(Date date) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	protected Tournament startTournament(int number, Date date) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	protected Tournament finishTournament(int number, Date date) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	protected Collection<TournamentGroup> loadFinishedUnprocessedGroups() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	protected TournamentSubscription loadSubscription(int tournament, Player player, Language language) {
		final HibernateTournamentSubscription.PK PK = new HibernateTournamentSubscription.PK(tournament, player, language);
		return (TournamentSubscription) sessionFactory.getCurrentSession().get(HibernateTournamentSubscription.class, PK);
	}

	@Override
	protected TournamentSubscription saveSubscription(int tournament, Player player, Language language, TournamentSection section) {
		HibernateTournamentSubscription subscription = new HibernateTournamentSubscription(tournament, player.getId(), language, section);
		sessionFactory.getCurrentSession().save(subscription);
		return subscription;
	}

	@Override
	protected Tournament loadTournament(int number) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	protected TournamentRound loadTournamentRound(int tournament, Language language, TournamentSection section, int round) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	protected TournamentGroup loadTournamentGroup(int tournament, Language language, TournamentSection section, int round) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	protected TournamentSubscription deleteSubscription(int tournament, Player player, Language language) {
		final TournamentSubscription subscription = loadSubscription(tournament, player, language);
		if (subscription != null) {
			sessionFactory.getCurrentSession().delete(subscription);
		}
		return subscription;
	}


	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public <T extends TournamentEntity> T getTournamentEntity(TournamentEntityId<T> id) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public <T extends TournamentEntity> List<T> searchTournamentEntities(Personality person, TournamentEntityContext<T> context, SearchFilter filter, Orders orders, Range range) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public int getTotalCount(Personality person, TournamentEntityContext context) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public int getFilteredCount(Personality person, TournamentEntityContext context, SearchFilter filter) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public List<TournamentEntity> searchEntities(Personality person, TournamentEntityContext context, SearchFilter filter, Orders orders, Range range) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}
}