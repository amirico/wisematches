package wisematches.playground.tournament.upcoming.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.quartz.CronExpression;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Language;
import wisematches.personality.player.Player;
import wisematches.playground.RatingManager;
import wisematches.playground.timer.BreakingDayListener;
import wisematches.playground.tournament.TournamentSection;
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

	private HibernateAnnouncementInfo announcementInfo = null;
	private final Map<Language, int[]> announcementDetails = new HashMap<Language, int[]>();

	public HibernateTournamentSubscriptionManager() {
		initAnnouncementDetails();
	}

	@Override
	public TournamentAnnouncement getTournamentAnnouncement(Language language) {
		lock.lock();
		try {
			if (language == null) {
				throw new NullPointerException("Language can't be null");
			}

			if (announcementInfo == null) {
				return null;
			}
			return new DefaultTournamentAnnouncement(announcementInfo, announcementDetails.get(language));
		} finally {
			lock.unlock();
		}
	}

	@Override
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
				session.save(request);
			} else {
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
				session.delete(request);
				firePlayerUnsubscribed(request);
			}
			return request;
		} finally {
			lock.unlock();
		}
	}

	@Override
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
	public Collection<TournamentRequest> getTournamentRequests(int announcement, Player player) throws WrongAnnouncementException {
		lock.lock();
		try {
			checkAnnouncementInfo(announcement);
			if (player == null) {
				throw new NullPointerException("Player can't be null");
			}
			final Session session = sessionFactory.getCurrentSession();
			final Query query = session.createQuery("select from wisematches.playground.tournament.upcoming.impl.HibernateTournamentRequest where tournament =  ? and player = ?");
			query.setParameter(0, announcement);
			query.setParameter(1, player.getId());
			return query.list();
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void breakingDayTime(Date midnight) {
		lock.lock();
		try {
			if (cronExpression.isSatisfiedBy(midnight)) {
				final Session session = sessionFactory.getCurrentSession();
				final Date nextTime = cronExpression.getNextValidTimeAfter(midnight);
				final int currentNumber = announcementInfo != null ? announcementInfo.getNumber() : 0;

				if (announcementInfo != null) {
					announcementInfo.setClosed(true);
					session.update(announcementInfo);
				}

				announcementInfo = new HibernateAnnouncementInfo(currentNumber + 1, nextTime);
				session.save(announcementInfo);

				initAnnouncementDetails();

//				fireTournamentAnnounced(new DefaultTournamentAnnouncement(announcementInfo, ));
			}
		} finally {
			lock.unlock();
		}
	}

	private void initAnnouncementDetails() {
		final int sections = TournamentSection.values().length;
		for (Language language : Language.values()) {
			announcementDetails.put(language, new int[sections]);
		}
	}

	private void checkAnnouncementInfo(int announcement) throws WrongAnnouncementException {
		if (this.announcementInfo == null) {
			throw new WrongAnnouncementException(announcement, 0, "No active announcements");
		}

		if (this.announcementInfo.getNumber() != announcement) {
			throw new WrongAnnouncementException(announcement, this.announcementInfo.getNumber());
		}
	}

	private void loadAnnouncement() {
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;

		if (this.cronExpression != null) {
			this.cronExpression.setTimeZone(timeZone);
		}
	}

	public void setCronExpression(String cronExpression) throws ParseException {
		this.cronExpression = new CronExpression(cronExpression);

		if (timeZone != null) {
			this.cronExpression.setTimeZone(timeZone);
		}
	}

	public void setRatingManager(RatingManager ratingManager) {
		this.ratingManager = ratingManager;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
