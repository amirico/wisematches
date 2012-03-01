package wisematches.playground.tournament.upcoming.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.quartz.CronExpression;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import wisematches.personality.Language;
import wisematches.personality.player.Player;
import wisematches.playground.RatingManager;
import wisematches.playground.search.descriptive.DescriptiveSearchManager;
import wisematches.playground.timer.BreakingDayListener;
import wisematches.playground.tournament.TournamentSection;
import wisematches.playground.tournament.TournamentSectionId;
import wisematches.playground.tournament.upcoming.TournamentAnnouncement;
import wisematches.playground.tournament.upcoming.TournamentRequest;
import wisematches.playground.tournament.upcoming.WrongAnnouncementException;
import wisematches.playground.tournament.upcoming.WrongSectionException;

import java.text.ParseException;
import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTournamentSubscriptionManager extends AbstractTournamentSubscriptionManager implements BreakingDayListener {
	private CronExpression cronExpression;
	private TimeZone timeZone = TimeZone.getTimeZone("GMT");

	private RatingManager ratingManager;
	private SessionFactory sessionFactory;
	private PlatformTransactionManager transactionManager;

	private HibernateTournamentAnnouncement announcement = null;

	public HibernateTournamentSubscriptionManager() {
	}

	@Override
	public TournamentAnnouncement getTournamentAnnouncement() {
		lock.lock();
		try {
			if (announcement == null) {
				return null;
			}
			return announcement;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public TournamentAnnouncement getTournamentAnnouncement(int number) {
		lock.lock();
		try {
			if (announcement != null && announcement.getNumber() == number) {
				return announcement;
			}
			return loadAnnouncement(number);
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public TournamentRequest subscribe(int announcement, Player player, Language language, TournamentSection section) throws WrongAnnouncementException, WrongSectionException {
		lock.lock();
		try {
			checkAnnouncementInfo(announcement);
			if (player == null) {
				throw new NullPointerException("Player can't be null");
			}
			if (language == null) {
				throw new NullPointerException("Player can't be null");
			}
			if (section == null) {
				throw new NullPointerException("Player can't be null");
			}

			final short rating = ratingManager.getRating(player);
			if (!section.isRatingAllowed(rating)) {
				throw new WrongSectionException(rating, section.getTopRating());
			}

			final Session session = sessionFactory.getCurrentSession();
			HibernateTournamentRequest request = (HibernateTournamentRequest) getTournamentRequest(announcement, player, language);
			if (request == null) {
				request = new HibernateTournamentRequest(announcement, player.getId(), language, section);
				this.announcement.changeBoughtTickets(language, section, 1);
				session.save(request);
			} else {
				this.announcement.changeBoughtTickets(language, section, 1);
				this.announcement.changeBoughtTickets(language, request.getSection(), -1);
				request.setTournamentSection(section);
				session.update(request);
			}
			firePlayerSubscribed(request);
			return request;
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public TournamentRequest unsubscribe(int announcement, Player player, Language language) throws WrongAnnouncementException {
		lock.lock();
		try {
			checkAnnouncementInfo(announcement);
			if (player == null) {
				throw new NullPointerException("Player can't be null");
			}
			if (language == null) {
				throw new NullPointerException("Player can't be null");
			}
			Session session = sessionFactory.getCurrentSession();
			HibernateTournamentRequest request = (HibernateTournamentRequest) getTournamentRequest(announcement, player, language);
			if (request != null) {
				this.announcement.changeBoughtTickets(language, request.getSection(), -1);
				session.delete(request);
				firePlayerUnsubscribed(request);
			}
			return request;
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public TournamentRequest getTournamentRequest(int announcement, Player player, Language language) throws WrongAnnouncementException {
		lock.lock();
		try {
			checkAnnouncementInfo(announcement);
			if (player == null) {
				throw new NullPointerException("Player can't be null");
			}
			if (language == null) {
				throw new NullPointerException("Player can't be null");
			}
			final HibernateTournamentRequest.PK PK = new HibernateTournamentRequest.PK(announcement, player, language);
			return (TournamentRequest) sessionFactory.getCurrentSession().get(HibernateTournamentRequest.class, PK);
		} finally {
			lock.unlock();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Collection<TournamentRequest> getTournamentRequests(int announcement, Player player) throws WrongAnnouncementException {
		lock.lock();
		try {
			checkAnnouncementInfo(announcement);
			if (player == null) {
				throw new NullPointerException("Player can't be null");
			}
			final Session session = sessionFactory.getCurrentSession();
			final Criteria criteria = session.createCriteria(HibernateTournamentRequest.class)
					.add(Restrictions.eq("announcement", announcement))
					.add(Restrictions.eq("player", player.getId()));
			return criteria.list();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public DescriptiveSearchManager<TournamentRequest, TournamentSectionId> getRequestsSearchManager() {
//		DescriptiveRequestSearchManager hibernateRequestSearchManager = new DescriptiveRequestSearchManager();
//		hibernateRequestSearchManager.setSessionFactory(sessionFactory);
//		return hibernateRequestSearchManager;
		return null;
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
					final HibernateTournamentAnnouncement old = announcement;
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

	private void initAnnouncement() {
		if (sessionFactory == null || cronExpression == null || timeZone == null || transactionManager == null) {
			return;
		}

		final DefaultTransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		final TransactionTemplate template = new TransactionTemplate(transactionManager, definition);
		template.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				final Session session = sessionFactory.getCurrentSession();
				announcement = loadAnnouncement(-1);

				if (announcement == null) {
					announcement = createAnnouncement(announcement);
					session.save(announcement);
				} else {
					breakingDayTime(getMidnight());
				}
			}
		});
	}

	private HibernateTournamentAnnouncement loadAnnouncement(int number) {
		final Session session = sessionFactory.getCurrentSession();
		HibernateTournamentAnnouncement res;
		if (number == -1) {
			final Criteria ann = session.createCriteria(HibernateTournamentAnnouncement.class)
					.add(Restrictions.eq("closed", false))
					.addOrder(Order.desc("number"))
					.setMaxResults(1);
			res = (HibernateTournamentAnnouncement) ann.uniqueResult();
		} else {
			res = (HibernateTournamentAnnouncement) session.get(HibernateTournamentAnnouncement.class, number);
		}

		if (res != null) {
			final Criteria values = session.createCriteria(HibernateTournamentRequest.class, "request")
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
		return res;
	}

	private HibernateTournamentAnnouncement createAnnouncement(final HibernateTournamentAnnouncement current) {
		final Date nextTime = getNextAnnouncementTime();
		final int currentNumber = current != null ? current.getNumber() : 0;
		return new HibernateTournamentAnnouncement(currentNumber + 1, nextTime);
	}

	private Date getMidnight() {
		return new Date((System.currentTimeMillis() / 86400000L) * 86400000L);
	}

	private Date getNextAnnouncementTime() {
		return cronExpression.getNextValidTimeAfter(getMidnight());
	}

	private void checkAnnouncementInfo(int announcement) throws WrongAnnouncementException {
		if (this.announcement == null) {
			throw new WrongAnnouncementException(announcement, 0, "No active announcements");
		}

		if (this.announcement.getNumber() != announcement) {
			throw new WrongAnnouncementException(announcement, this.announcement.getNumber());
		}
	}

	public void setTimeZone(TimeZone timeZone) {
		if (timeZone == null) {
			throw new NullPointerException("TimeZone can't be null");
		}

		lock.lock();
		try {
			this.timeZone = timeZone;

			if (this.cronExpression != null) {
				this.cronExpression.setTimeZone(timeZone);
			}
			initAnnouncement();
		} finally {
			lock.unlock();
		}
	}

	public void setCronExpression(String cronExpression) throws ParseException {
		if (cronExpression == null) {
			throw new NullPointerException("Cron expression can't be null");
		}

		final CronExpression exp = new CronExpression(cronExpression);
		lock.lock();
		try {
			this.cronExpression = exp;

			if (timeZone != null) {
				this.cronExpression.setTimeZone(timeZone);
			}
			initAnnouncement();
		} finally {
			lock.unlock();
		}
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		lock.lock();
		try {
			this.sessionFactory = sessionFactory;
			initAnnouncement();
		} finally {
			lock.unlock();
		}
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		lock.lock();
		try {
			this.transactionManager = transactionManager;
			initAnnouncement();
		} finally {
			lock.unlock();
		}
	}

	public void setRatingManager(RatingManager ratingManager) {
		lock.lock();
		try {
			this.ratingManager = ratingManager;
		} finally {
			lock.unlock();
		}
	}
}
