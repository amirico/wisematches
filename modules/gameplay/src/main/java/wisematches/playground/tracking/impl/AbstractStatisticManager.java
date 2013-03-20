package wisematches.playground.tracking.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.*;
import wisematches.playground.*;
import wisematches.playground.tourney.TourneyWinner;
import wisematches.playground.tourney.regular.RegularTourneyListener;
import wisematches.playground.tourney.regular.RegularTourneyManager;
import wisematches.playground.tourney.regular.Tourney;
import wisematches.playground.tourney.regular.TourneyDivision;
import wisematches.playground.tracking.StatisticManager;
import wisematches.playground.tracking.Statistics;
import wisematches.playground.tracking.StatisticsListener;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractStatisticManager<S extends Statistics, E extends StatisticsEditor> implements StatisticManager<S> {
	private PersonalityManager personalityManager;
	private GamePlayManager gamePlayManager;
	private RegularTourneyManager tourneyManager;

	private final StatisticsTrapper<E> statisticsTrapper;

	private final Lock statisticLock = new ReentrantLock();
	private final Map<Personality, Short> ratingsCache = new WeakHashMap<>();

	private final PersonalityListener playerListener = new ThePersonalityListener();
	private final BoardListener gamePlayListener = new TheBoardListener();
	private final RegularTourneyListener tourneyListener = new TheRegularTourneyListener();


	private final Set<StatisticsListener> statisticsListeners = new CopyOnWriteArraySet<>();

	private static final Logger log = LoggerFactory.getLogger("wisematches.playground.tracking");

	protected AbstractStatisticManager(StatisticsTrapper<E> statisticsTrapper) {
		this.statisticsTrapper = statisticsTrapper;
	}

	@Override
	public void addStatisticListener(StatisticsListener l) {
		if (l != null) {
			statisticsListeners.add(l);
		}
	}

	@Override
	public void removeStatisticListener(StatisticsListener l) {
		if (l != null) {
			statisticsListeners.remove(l);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS)
	public S getStatistic(Player personality) {
		statisticLock.lock();
		try {
			if (personality instanceof Member) {
				return (S) loadStatisticEditor((Member) personality);
			}
			return null;
		} finally {
			statisticLock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public short getRating(final Player person) {
		if (person instanceof Member) {
			Short rating = ratingsCache.get(person);
			if (rating == null) {
				Number n = loadPlayerRating((Member) person);
				rating = (n == null ? 1200 : n.shortValue());
				ratingsCache.put(person, rating);
			}
			return rating;
		}
		return 1200;
	}

	public void setGamePlayManager(GamePlayManager gamePlayManager) {
		if (this.gamePlayManager != null) {
			this.gamePlayManager.removeBoardListener(gamePlayListener);
		}
		this.gamePlayManager = gamePlayManager;

		if (this.gamePlayManager != null) {
			this.gamePlayManager.addBoardListener(gamePlayListener);
		}
	}

	public void setPersonalityManager(PersonalityManager personalityManager) {
		if (this.personalityManager != null) {
			this.personalityManager.removePersonalityListener(playerListener);
		}

		this.personalityManager = personalityManager;

		if (this.personalityManager != null) {
			this.personalityManager.addPersonalityListener(playerListener);
		}
	}

	public void setTourneyManager(RegularTourneyManager tourneyManager) {
		if (this.tourneyManager != null) {
			this.tourneyManager.removeRegularTourneyListener(tourneyListener);
		}

		this.tourneyManager = tourneyManager;

		if (this.tourneyManager != null) {
			this.tourneyManager.addRegularTourneyListener(tourneyListener);
		}
	}

	@SuppressWarnings("unchecked")
	protected void processGameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand, ? extends GameMove> board) {
		final Collection<Personality> players = board.getPlayers();
		for (Personality personality : players) {
			if (personality instanceof Member) {
				final Member member = (Member) personality;
				statisticLock.lock();
				try {
					final E editor = loadStatisticEditor(member);
					statisticsTrapper.trapGameStarted(member, editor);
					saveStatisticEditor(editor);
					fireStatisticUpdated(member, editor);
				} catch (Throwable th) {
					log.error("Statistic can't be updated for member: {}", personality, th);
				} finally {
					statisticLock.unlock();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void processGameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand, ? extends GameMove> board, GameMove move, GameMoveScore moveScore) {
		final Personality personality = move.getPlayer();
		if (personality instanceof Member) {
			final Member member = (Member) personality;
			statisticLock.lock();
			try {
				final E editor = loadStatisticEditor(member);
				statisticsTrapper.trapGameMoveDone(member, editor, board, move, moveScore);
				saveStatisticEditor(editor);
				fireStatisticUpdated(member, editor);
			} catch (Throwable th) {
				log.error("Statistic can't be updated for member: {}", member, th);
			} finally {
				statisticLock.unlock();
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void processGameFinished(GameBoard<?, ?, ?> board) {
		final Collection<Personality> hands = board.getPlayers();
		for (Personality personality : hands) {
			if (personality instanceof Member) {
				final Member member = (Member) personality;
				statisticLock.lock();
				try {
					final E editor = loadStatisticEditor(member);
					statisticsTrapper.trapGameFinished(member, editor, board);
					saveStatisticEditor(editor);
					fireStatisticUpdated(member, editor);
				} catch (Throwable th) {
					log.error("Statistic can't be updated for player: {}", member, th);
				} finally {
					statisticLock.unlock();
				}
			}
		}
	}

	protected void processTourneyFinished(TourneyDivision division) {
		final Collection<TourneyWinner> tourneyWinners = division.getTourneyWinners();
		for (TourneyWinner winner : tourneyWinners) {
			final Member member = personalityManager.getMember(winner.getPlayer());
			if (member == null) {
				continue;
			}
			statisticLock.lock();
			try {
				final E editor = loadStatisticEditor(member);
				statisticsTrapper.trapTourneyFinished(member, editor, winner.getPlace());
				saveStatisticEditor(editor);
				fireStatisticUpdated(member, editor);
			} catch (Throwable th) {
				log.error("Statistic can't be updated for member: {}", member, th);
			} finally {
				statisticLock.unlock();
			}
		}
	}

	protected void fireStatisticUpdated(Member player, StatisticsEditor statistic) {
		final Set<String> strings = statistic.takeChangedProperties();
		for (StatisticsListener statisticsListener : statisticsListeners) {
			statisticsListener.playerStatisticUpdated(player, statistic, strings);
		}
	}

	protected abstract E createStatistic(Member player);

	protected abstract void removeStatistic(Member player);


	protected abstract E loadStatisticEditor(Member person);

	protected abstract void saveStatisticEditor(E statisticEditor);


	protected abstract Number loadPlayerRating(Member person);

	private class ThePersonalityListener implements PersonalityListener {
		private ThePersonalityListener() {
		}

		@Override
		public void playerRegistered(Personality player) {
			if (player instanceof Member) {
				createStatistic((Member) player);
			}
		}

		@Override
		public void playerUnregistered(Personality player) {
			if (player instanceof Member) {
				removeStatistic((Member) player);
			}
		}
	}

	private class TheBoardListener implements BoardListener {
		private TheBoardListener() {
		}

		@Override
		public void gameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand, ? extends GameMove> board) {
			processGameStarted(board);
		}

		@Override
		public void gameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand, ? extends GameMove> board, GameMove move, GameMoveScore moveScore) {
			processGameMoveDone(board, move, moveScore);
		}

		@Override
		public void gameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand, ? extends GameMove> board, GameResolution resolution, Collection<Personality> winners) {
			processGameFinished(board);
		}
	}

	private class TheRegularTourneyListener implements RegularTourneyListener {
		private TheRegularTourneyListener() {
		}

		@Override
		public void tourneyAnnounced(Tourney tourney) {
		}

		@Override
		public void tourneyStarted(Tourney tourney) {
		}

		@Override
		public void tourneyFinished(Tourney tourney, TourneyDivision division) {
			processTourneyFinished(division);
		}
	}
}
