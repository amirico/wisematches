package wisematches.playground.tournament.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronExpression;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Language;
import wisematches.personality.player.Player;
import wisematches.playground.*;
import wisematches.playground.timer.BreakingDayListener;
import wisematches.playground.tournament.*;

import java.util.Collection;
import java.util.Date;
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

	private BoardManager boardManager;
	private RatingManager ratingManager;

	private AnnouncementImpl tournamentAnnouncement;
//	private AssuredTaskExecutor<TSMActivity, Serializable> assuredTaskExecutor;

	private final Lock tournamentLock = new ReentrantLock();
	private final Lock subscriptionLock = new ReentrantLock();

	private final TheBoardStateListener boardStateListener = new TheBoardStateListener();

	private final Collection<TournamentStateListener> stateListeners = new CopyOnWriteArraySet<TournamentStateListener>();
	private final Collection<TournamentSubscriptionListener> subscriptionListeners = new CopyOnWriteArraySet<TournamentSubscriptionListener>();

	private static final Log log = LogFactory.getLog("wisematches.server.announcement");

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
			final Announcement announcement = loadAnnouncement();
			if (announcement != null) {
				tournamentAnnouncement = new AnnouncementImpl(announcement);
			} else {
				tournamentAnnouncement = new AnnouncementImpl(scheduleTournament(getMidnight()));
				fireTournamentStateChanged(tournamentAnnouncement, TournamentState.SCHEDULED);
			}
//			assuredTaskExecutor.registerProcessor(TSMActivity.createTaskProcessor(this));
		} finally {
			subscriptionLock.unlock();
		}
	}

	@Override
	public final Announcement getAnnouncement() {
		subscriptionLock.lock();
		try {
			return tournamentAnnouncement;
		} finally {
			subscriptionLock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public final TournamentSubscription subscribe(int announcement, Player player, Language language, TournamentSection section) throws WrongTournamentException, WrongSectionException {
		subscriptionLock.lock();
		try {
			checkAnnouncement(announcement, player, language);
			if (section == null) {
				throw new NullPointerException("Tournament section can't be null");
			}

			final short rating = ratingManager.getRating(player);
			if (!section.isRatingAllowed(rating)) {
				throw new WrongSectionException(rating, section.getTopRating());
			}

			final TournamentSubscription unsubscription = deleteSubscription(announcement, player, language);
			if (unsubscription != null) {
				tournamentAnnouncement.changeBoughtTickets(unsubscription.getLanguage(), unsubscription.getSection(), -1);
				firePlayerUnsubscribed(unsubscription);
			}

			final TournamentSubscription subscription = saveSubscription(announcement, player, language, section);
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
	public final TournamentSubscription unsubscribe(int announcement, Player player, Language language) throws WrongTournamentException {
		subscriptionLock.lock();
		try {
			checkAnnouncement(announcement, player, language);

			final TournamentSubscription unsubscription = deleteSubscription(announcement, player, language);
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
	public void breakingDayTime(Date midnight) {
		if (cronExpression.isSatisfiedBy(midnight)) { // new announcement time
//			assuredTaskExecutor.execute(TSMActivity.INITIATE_TOURNAMENT, getNextTournamentDate());
		}

		tournamentLock.lock();
		try {
			Collection<TournamentGroup> unprocessedGroups = loadFinishedUnprocessedGroups();
		} finally {
			tournamentLock.unlock();
		}
	}

	protected abstract Announcement loadAnnouncement();

	protected abstract Collection<TournamentSubscription> loadSubscriptions(int tournament, Player player);

	protected abstract TournamentSubscription loadSubscription(int tournament, Player player, Language language);

	protected abstract TournamentSubscription deleteSubscription(int tournament, Player player, Language language);

	protected abstract TournamentSubscription saveSubscription(int tournament, Player player, Language language, TournamentSection section);


	protected abstract Tournament loadTournament(int number);

	protected abstract Tournament scheduleTournament(Date date);

	protected abstract Tournament startTournament(int number, Date date);

	protected abstract Tournament finishTournament(int number, Date date);


//	protected abstract Collection<TournamentSubscription> loadSubscriptions(int announcement);
//
//	protected abstract Collection<TournamentSubscription> loadRoundWinners(int announcement, int round);

	protected abstract Collection<TournamentGroup> loadFinishedUnprocessedGroups();


	protected abstract TournamentRound loadTournamentRound(int tournament, Language language, TournamentSection section, int round);

	protected abstract TournamentGroup loadTournamentGroup(int tournament, Language language, TournamentSection section, int round);

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


	private Date getMidnight() {
		return new Date((System.currentTimeMillis() / 86400000L) * 86400000L);
	}

	private Date getNextTournamentDate() {
		return cronExpression.getNextValidTimeAfter(getMidnight());
	}

	private void checkAnnouncement(int tournament, Player player, Language language) throws WrongTournamentException {
		final Tournament scheduledTournament = getAnnouncement();
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

	public void setBoardManager(BoardManager boardManager) {
		if (this.boardManager != null) {
			this.boardManager.removeBoardStateListener(boardStateListener);
		}

		this.boardManager = boardManager;

		if (this.boardManager != null) {
			this.boardManager.addBoardStateListener(boardStateListener);
		}
	}

	public void setRatingManager(RatingManager ratingManager) {
		this.ratingManager = ratingManager;
	}

/*
	public void setAssuredTaskExecutor(AssuredTaskExecutor<TSMActivity, Serializable> assuredTaskExecutor) {
		this.assuredTaskExecutor = assuredTaskExecutor;
	}
*/

	private final class TheBoardStateListener implements BoardStateListener {
		private TheBoardStateListener() {
		}

		@Override
		public void gameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board) {
		}

		@Override
		public void gameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move, GameMoveScore moveScore) {
		}

		@Override
		public void gameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameResolution resolution, Collection<? extends GamePlayerHand> winners) {
//			assuredTaskExecutor.execute(TSMActivity.FINALIZE_GAME, board.getBoardId());
		}
	}
}
