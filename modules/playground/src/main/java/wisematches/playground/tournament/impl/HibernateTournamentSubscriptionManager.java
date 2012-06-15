package wisematches.playground.tournament.impl;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Deprecated
public class HibernateTournamentSubscriptionManager {
/*
	public HibernateTournamentSubscriptionManager() {
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getTotalCount(Personality person, TournamentSectionId context) {
		return getFilteredCount(person, context, null);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getFilteredCount(Personality person, TournamentSectionId context, SearchFilter filter) {
		lock.lock();
		try {
			if (context == null) {
				throw new NullPointerException("Context can't be null");
			}

			final Criteria criteria = createSearchCriteria(person, context).setProjection(Projections.rowCount());
			return ((Number) criteria.uniqueResult()).intValue();
		} finally {
			lock.unlock();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<TournamentSubscription> searchEntities(Personality person, TournamentSectionId context, SearchFilter filter, Orders orders, Range range) {
		lock.lock();
		try {
			if (context == null) {
				throw new NullPointerException("Context can't be null");
			}
			final Criteria criteria = createSearchCriteria(person, context);
			if (orders != null) {
				orders.apply(criteria);
			}
			if (range != null) {
				range.apply(criteria);
			}
			return criteria.list();
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void breakingDayTime(Date midnight) {
		lock.lock();
		try {
			final Session session = sessionFactory.getCurrentSession();

			if (announcement == null) { // no announcement
				if (cronExpression.isSatisfiedBy(midnight)) { // but it's time for new one
					announcement = createAnnouncement(announcement);
					session.save(announcement);
					fireTournamentAnnounced(null, announcement);
				}
			} else {
				// not closed and scheduled for today or in past
				if (!announcement.isClosed() && announcement.getScheduledDate().compareTo(midnight) <= 0) {
					final TournamentAnnouncementImpl old = announcement;
					old.setClosed(true);
					session.update(old);

					announcement = createAnnouncement(announcement);
					session.save(announcement);
					fireTournamentAnnounced(old, announcement);
				}
			}
		} finally {
			lock.unlock();
		}
	}

	private Criteria createSearchCriteria(Personality person, TournamentSectionId context) {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria criteria = session.createCriteria(HibernateTournamentSubscription.class)
				.add(Restrictions.eq("pk.announcement", context.getTournament()))
				.add(Restrictions.eq("pk.language", context.getLanguage()))
				.add(Restrictions.eq("section", context.getTournamentCategory()));
		if (person != null) {
			criteria.add(Restrictions.eq("pk.player", person.getId()));
		}
		return criteria;
	}
*/
}
