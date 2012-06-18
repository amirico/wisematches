package wisematches.playground.tournament.impl;

import org.quartz.CronExpression;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.database.Orders;
import wisematches.database.Range;
import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.playground.RatingManager;
import wisematches.playground.search.SearchFilter;
import wisematches.playground.task.AssuredTaskExecutor;
import wisematches.playground.timer.BreakingDayListener;
import wisematches.playground.tournament.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractTournamentManager implements TournamentManager, BreakingDayListener, InitializingBean {
	private CronExpression cronExpression;
	private TimeZone timeZone = TimeZone.getTimeZone("GMT");
	private RatingManager ratingManager;

	private TournamentAnnouncementImpl tournamentAnnouncement;
	private AssuredTaskExecutor<Serializable, Serializable> assuredTaskExecutor;

	private final Lock tournamentLock = new ReentrantLock();
	private final Lock subscriptionLock = new ReentrantLock();

	private final Collection<TournamentStateListener> stateListeners = new CopyOnWriteArraySet<TournamentStateListener>();
	private final Collection<TournamentProgressListener> progressListeners = new CopyOnWriteArraySet<TournamentProgressListener>();
	private final Collection<TournamentSubscriptionListener> subscriptionListeners = new CopyOnWriteArraySet<TournamentSubscriptionListener>();

	protected AbstractTournamentManager() {
	}

	@Override
	public void addTournamentStateListener(TournamentStateListener l) {
		if (l != null) {
			stateListeners.add(l);
		}
	}

	@Override
	public void removeTournamentStateListener(TournamentStateListener l) {
		stateListeners.remove(l);
	}

	@Override
	public void addTournamentProgressListener(TournamentProgressListener l) {
		if (l != null) {
			progressListeners.add(l);
		}
	}

	@Override
	public void removeTournamentProgressListener(TournamentProgressListener l) {
		progressListeners.remove(l);
	}

	@Override
	public void addTournamentSubscriptionListener(TournamentSubscriptionListener l) {
		if (subscriptionListeners != null) {
			subscriptionListeners.add(l);
		}
	}

	@Override
	public void removeTournamentSubscriptionListener(TournamentSubscriptionListener l) {
		subscriptionListeners.remove(l);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void afterPropertiesSet() throws Exception {
		subscriptionLock.lock();
		try {
			tournamentAnnouncement = loadAnnouncement();
			if (tournamentAnnouncement == null) {
				tournamentAnnouncement = createAnnouncement(getNextTournamentDate());
				fireTournamentStateChanged(tournamentAnnouncement, TournamentState.SCHEDULED);
			}
		} finally {
			subscriptionLock.unlock();
		}
	}

	@Override
	public final TournamentAnnouncement getTournamentAnnouncement() {
		subscriptionLock.lock();
		try {
			return tournamentAnnouncement;
		} finally {
			subscriptionLock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public final TournamentSubscription subscribe(int tournament, Player player, Language language, TournamentSection section) throws WrongTournamentException, WrongSectionException {
		subscriptionLock.lock();
		try {
			checkAnnouncement(tournament, player, language);
			if (section == null) {
				throw new NullPointerException("Tournament section can't be null");
			}

			final short rating = ratingManager.getRating(player);
			if (!section.isRatingAllowed(rating)) {
				throw new WrongSectionException(rating, section.getTopRating());
			}

			final TournamentSubscription unsubscription = deleteSubscription(tournament, player, language);
			if (unsubscription != null) {
				tournamentAnnouncement.changeBoughtTickets(unsubscription.getLanguage(), unsubscription.getSection(), -1);
				firePlayerUnsubscribed(unsubscription);
			}

			final TournamentSubscription subscription = saveSubscription(tournament, player, language, section);
			if (subscription != null) {
				tournamentAnnouncement.changeBoughtTickets(subscription.getLanguage(), subscription.getSection(), 1);
				firePlayerSubscribed(subscription);
			}
			return subscription;
		} finally {
			subscriptionLock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public final TournamentSubscription unsubscribe(int tournament, Player player, Language language) throws WrongTournamentException {
		subscriptionLock.lock();
		try {
			checkAnnouncement(tournament, player, language);

			final TournamentSubscription unsubscription = deleteSubscription(tournament, player, language);
			if (unsubscription != null) {
				tournamentAnnouncement.changeBoughtTickets(unsubscription.getLanguage(), unsubscription.getSection(), -1);
				firePlayerUnsubscribed(unsubscription);
			}
			return unsubscription;
		} finally {
			subscriptionLock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public final TournamentSubscription getSubscription(int tournament, Player player, Language language) throws WrongTournamentException {
		subscriptionLock.lock();
		try {
			checkAnnouncement(tournament, player, language);
			return loadSubscription(tournament, player, language);
		} finally {
			subscriptionLock.unlock();
		}
	}


	@Override
	public Tournament getTournament(int number) {
		tournamentLock.lock();
		try {
			throw new UnsupportedOperationException("TODO: Not implemented");
		} finally {
			tournamentLock.unlock();
		}
	}

	@Override
	public TournamentRound getTournamentRound(int tournament, Language language, TournamentSection section, int round) {
		tournamentLock.lock();
		try {
			throw new UnsupportedOperationException("TODO: Not implemented");
		} finally {
			tournamentLock.unlock();
		}
	}

	@Override
	public TournamentGroup getTournamentGroup(int tournament, Language language, TournamentSection section, int round, int group) {
		tournamentLock.lock();
		try {
			throw new UnsupportedOperationException("TODO: Not implemented");
		} finally {
			tournamentLock.unlock();
		}
	}

	@Override
	public int getTotalCount(Personality person, TournamentEntityCtx<TournamentEntity> context) {
		tournamentLock.lock();
		try {
			throw new UnsupportedOperationException("TODO: Not implemented");
		} finally {
			tournamentLock.unlock();
		}
	}

	@Override
	public int getFilteredCount(Personality person, TournamentEntityCtx<TournamentEntity> context, SearchFilter filter) {
		tournamentLock.lock();
		try {
			throw new UnsupportedOperationException("TODO: Not implemented");
		} finally {
			tournamentLock.unlock();
		}
	}

	@Override
	public List<TournamentEntity> searchEntities(Personality person, TournamentEntityCtx<TournamentEntity> context, SearchFilter filter, Orders orders, Range range) {
		tournamentLock.lock();
		try {
			throw new UnsupportedOperationException("TODO: Not implemented");
		} finally {
			tournamentLock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void breakingDayTime(Date midnight) {
		tournamentLock.lock();
		subscriptionLock.lock();
		try {
			// TODO: start new tournament here
			if (cronExpression.isSatisfiedBy(midnight)) { // but it's time for new one

			} else {

			}
		} finally {
			subscriptionLock.unlock();
			tournamentLock.unlock();
		}
	}

	protected abstract TournamentAnnouncementImpl loadAnnouncement();

	protected abstract TournamentAnnouncementImpl createAnnouncement(Date scheduledDate);

	protected abstract TournamentSubscription loadSubscription(int tournament, Player player, Language language);

	protected abstract TournamentSubscription deleteSubscription(int tournament, Player player, Language language);

	protected abstract TournamentSubscription saveSubscription(int tournament, Player player, Language language, TournamentSection section);


	private void fireTournamentStateChanged(Tournament tournament, TournamentState state) {
		for (TournamentStateListener stateListener : stateListeners) {
			switch (state) {
				case STARTED:
					stateListener.tournamentStarted(tournament);
					break;
				case FINISHED:
					stateListener.tournamentFinished(tournament);
					break;
				case SCHEDULED:
					stateListener.tournamentScheduled(tournament);
					break;
				default:
					throw new IllegalArgumentException("Unsupported state: " + state);
			}
		}
	}

	private void fireSectionFinished(Tournament tournament, TournamentSection section) {
		for (TournamentProgressListener progressListener : progressListeners) {
			progressListener.tournamentSectionFinished(tournament, section);
		}
	}

	private void fireRoundFinished(Tournament tournament, TournamentRound round) {
		for (TournamentProgressListener progressListener : progressListeners) {
			progressListener.tournamentRoundFinished(tournament, round);
		}
	}

	private void fireGroupFinished(Tournament tournament, TournamentGroup group) {
		for (TournamentProgressListener progressListener : progressListeners) {
			progressListener.tournamentGroupFinished(tournament, group);
		}
	}

	private void firePlayerSubscribed(TournamentSubscription subscription) {
		for (TournamentSubscriptionListener subscriptionListener : subscriptionListeners) {
			subscriptionListener.playerSubscribed(subscription);
		}
	}

	private void firePlayerUnsubscribed(TournamentSubscription subscription) {
		for (TournamentSubscriptionListener subscriptionListener : subscriptionListeners) {
			subscriptionListener.playerUnsubscribed(subscription);
		}
	}


	private Date getMidnight() {
		return new Date((System.currentTimeMillis() / 86400000L) * 86400000L);
	}

	private Date getNextTournamentDate() {
		return cronExpression.getNextValidTimeAfter(getMidnight());
	}

	private void checkAnnouncement(int tournament, Player player, Language language) throws WrongTournamentException {
		final Tournament scheduledTournament = getTournamentAnnouncement();
		if (scheduledTournament == null) {
			throw new WrongTournamentException(tournament, 0, "No active announcements");
		}

		if (scheduledTournament.getNumber() != tournament) {
			throw new WrongTournamentException(tournament, scheduledTournament.getNumber());
		}

		if (player == null) {
			throw new NullPointerException("Player can't be null");
		}
		if (language == null) {
			throw new NullPointerException("Player can't be null");
		}
	}

	public void setTimeZone(TimeZone timeZone) {
		if (timeZone == null) {
			throw new NullPointerException("TimeZone can't be null");
		}

		this.timeZone = timeZone;

		if (this.cronExpression != null) {
			this.cronExpression.setTimeZone(timeZone);
		}
	}

	public void setCronExpression(CronExpression cronExpression) {
		this.cronExpression = cronExpression;

		if (this.cronExpression != null) {
			this.cronExpression.setTimeZone(timeZone);
		}
	}

	public void setRatingManager(RatingManager ratingManager) {
		this.ratingManager = ratingManager;
	}

	public void setAssuredTaskExecutor(AssuredTaskExecutor<Serializable, Serializable> assuredTaskExecutor) {
		this.assuredTaskExecutor = assuredTaskExecutor;
	}
}
