package wisematches.playground.tournament.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.PlatformTransactionManager;
import wisematches.personality.Language;
import wisematches.personality.player.Player;
import wisematches.playground.tournament.Tournament;
import wisematches.playground.tournament.TournamentSection;
import wisematches.playground.tournament.TournamentSubscription;

import java.util.Date;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTournamentManager extends AbstractTournamentManager {
	private SessionFactory sessionFactory;
	private PlatformTransactionManager transactionManager;

	public HibernateTournamentManager() {
	}

	@Override
	protected TournamentAnnouncementImpl loadAnnouncement() {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria criteria = session.createCriteria(HibernateTournament.class);
		criteria.add(Restrictions.isNull("startedDate")).add(Restrictions.isNull("finishedDate"));
		final Tournament tournament = (Tournament) criteria.uniqueResult();
		if (tournament != null) {
			final TournamentAnnouncementImpl res = new TournamentAnnouncementImpl(tournament);
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
	protected TournamentAnnouncementImpl createAnnouncement(Date scheduledDate) {
		final HibernateTournament tournament = new HibernateTournament(scheduledDate);
		sessionFactory.getCurrentSession().save(tournament);
		return new TournamentAnnouncementImpl(tournament);
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

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
}