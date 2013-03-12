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
			return (S) loadStatisticEditor(personality);
		} finally {
			statisticLock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public short getRating(final Player person) {
		Short rating = ratingsCache.get(person);
		if (rating == null) {
			Number n = loadPlayerRating(person);
			rating = (n == null ? 1200 : n.shortValue());
			ratingsCache.put(person, rating);
		}
		return rating;
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
			if (personality instanceof Player) {
				final Player player = (Player) personality;
				statisticLock.lock();
				try {
					final E editor = loadStatisticEditor(player);
					statisticsTrapper.trapGameStarted(player, editor);
					saveStatisticEditor(editor);
					fireStatisticUpdated(player, editor);
				} catch (Throwable th) {
					log.error("Statistic can't be updated for player: {}", personality, th);
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
			final Player player = (Player) personality;
			statisticLock.lock();
			try {
				final E editor = loadStatisticEditor(player);
				statisticsTrapper.trapGameMoveDone(player, editor, board, move, moveScore);
				saveStatisticEditor(editor);
				fireStatisticUpdated(player, editor);
			} catch (Throwable th) {
				log.error("Statistic can't be updated for player: {}", player, th);
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
				final Player player = (Player) personality;
				statisticLock.lock();
				try {
					final E editor = loadStatisticEditor(player);
					statisticsTrapper.trapGameFinished(player, editor, board);
					saveStatisticEditor(editor);
					fireStatisticUpdated(player, editor);
				} catch (Throwable th) {
					log.error("Statistic can't be updated for player: {}", player, th);
				} finally {
					statisticLock.unlock();
				}
			}
		}
	}

	protected void processTourneyFinished(TourneyDivision division) {
		final Collection<TourneyWinner> tourneyWinners = division.getTourneyWinners();
		for (TourneyWinner winner : tourneyWinners) {
			final Member player = personalityManager.getMember(winner.getPlayer());
			if (player == null) {
				continue;
			}
			statisticLock.lock();
			try {
				final E editor = loadStatisticEditor(player);
				statisticsTrapper.trapTourneyFinished(player, editor, winner.getPlace());
				saveStatisticEditor(editor);
				fireStatisticUpdated(player, editor);
			} catch (Throwable th) {
				log.error("Statistic can't be updated for player: {}", player, th);
			} finally {
				statisticLock.unlock();
			}
		}
	}

	protected void fireStatisticUpdated(Player player, StatisticsEditor statistic) {
		final Set<String> strings = statistic.takeChangedProperties();
		for (StatisticsListener statisticsListener : statisticsListeners) {
			statisticsListener.playerStatisticUpdated(player, statistic, strings);
		}
	}

	protected abstract E createStatistic(Player player);

	protected abstract void removeStatistic(Player player);


	protected abstract E loadStatisticEditor(Personality person);

	protected abstract void saveStatisticEditor(E statisticEditor);


	protected abstract Number loadPlayerRating(Personality person);

	private class ThePersonalityListener implements PersonalityListener {
		private ThePersonalityListener() {
		}

		@Override
		public void playerRegistered(Personality player) {
			if (player instanceof Player) {
				createStatistic((Player) player);
			}
		}

		@Override
		public void playerUnregistered(Personality player) {
			if (player instanceof Player) {
				removeStatistic((Player) player);
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
