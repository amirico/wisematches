package wisematches.server.standing.rating.impl;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import wisematches.server.playground.board.*;
import wisematches.server.playground.room.RoomManager;
import wisematches.server.playground.room.RoomsManager;
import wisematches.server.playground.board.BoardManager;
import wisematches.server.playground.board.BoardStateListener;
import wisematches.server.personality.Personality;
import wisematches.server.personality.player.computer.ComputerPlayer;
import wisematches.server.standing.rating.*;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernatePlayerRatingManager extends HibernateDaoSupport implements PlayerRatingManager {
	private RoomsManager roomsManager;
	private RatingSystem ratingSystem;

	private final TheBoardStateListener boardStateListener = new TheBoardStateListener();

	private final Lock ratingsLock = new ReentrantLock();
	private final Map<Personality, Short> ratings = new WeakHashMap<Personality, Short>();
	private final Collection<PlayerRatingListener> ratingListeners = new CopyOnWriteArraySet<PlayerRatingListener>();

	private static final Logger log = Logger.getLogger(HibernatePlayerRatingManager.class);

	public HibernatePlayerRatingManager() {
	}

	@Override
	public void addRatingsChangeListener(PlayerRatingListener l) {
		ratingListeners.add(l);
	}

	@Override
	public void removeRatingsChangeListener(PlayerRatingListener l) {
		ratingListeners.remove(l);
	}

	@Override
	public short getRating(final Personality personality) {
		ratingsLock.lock();
		try {
			Short rating = ratings.get(personality);
			if (rating == null) {
				ComputerPlayer computerPlayer = ComputerPlayer.getComputerPlayer(personality.getId());
				if (computerPlayer != null) {
					rating = computerPlayer.getRating();
				} else {
					rating = getHibernateTemplate().execute(new HibernateCallback<Short>() {
						@Override
						public Short doInHibernate(Session session) throws HibernateException, SQLException {
							return ((Number) session.getNamedQuery("player.rating").setLong(0, personality.getId()).uniqueResult()).shortValue();
						}
					});
				}
				ratings.put(personality, rating);
			}
			return rating;
		} finally {
			ratingsLock.unlock();
		}
	}

	@Override
	public long getPosition(final Personality player) {
		return getHibernateTemplate().execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session) throws HibernateException, SQLException {
				return (Long) ((Object[]) session.getNamedQuery("player.position").setLong(0, player.getId()).uniqueResult())[0];
			}
		});
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<RatingChange> getRatingChanges(GameBoard board) {
		final Collection<RatingChange> ratingChanges = getHibernateTemplate().find(
				"from wisematches.server.standing.rating.RatingChange rating where rating.boardId = ?",
				board.getBoardId());

		if (ratingChanges != null) {
			final Collection<GamePlayerHand> hands = board.getPlayersHands();
			for (GamePlayerHand hand : hands) {
				final ComputerPlayer cp = ComputerPlayer.getComputerPlayer(hand.getPlayerId());
				if (cp != null) {
					final RatingChange value = new RatingChange(cp.getId(), board.getBoardId(), board.getFinishedTime(), cp.getRating(), cp.getRating(), hand.getPoints());
					ratingChanges.add(value);
				}
			}
			return ratingChanges;
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<RatingBatch> getRatingChanges(final Personality player, final RatingBatching batching) {
		return getHibernateTemplate().execute(new HibernateCallback<Collection<RatingBatch>>() {
			@Override
			public Collection<RatingBatch> doInHibernate(Session session) throws HibernateException, SQLException {
				final Query namedQuery = session.getNamedQuery(batching.getQueryName());
				namedQuery.setParameter("pid", player.getId());
				return namedQuery.list();
			}
		});
	}

	private void processRatingChange(final long playerId, final GameBoard board, final short oldRating, final short newRating, final short points) {
		ratingsLock.lock();
		try {
			final long boardId = board.getBoardId();
			final ComputerPlayer computerPlayer = ComputerPlayer.getComputerPlayer(playerId);
			if (computerPlayer != null) {
				log.debug("Computer rating can't be changed: " + playerId + " on board " + boardId + ": " + oldRating + "->" + newRating);
			} else {
				log.info("Process player's rating changed: " + playerId + " on board " + boardId + ": " + oldRating + "->" + newRating);

				final Personality person = Personality.person(playerId);
				final RatingChange entity = new RatingChange(playerId, boardId, new Date(), oldRating, newRating, points);
				if (log.isDebugEnabled()) {
					log.debug("RatingChange event: " + entity);
				}
				final HibernateTemplate template = getHibernateTemplate();
				template.save(entity); // create new record

				if (oldRating != newRating) { // update only if change is real
					HibernatePlayerRating rating = template.get(HibernatePlayerRating.class, playerId);
					if (rating == null) {
						rating = new HibernatePlayerRating(playerId, newRating);
						template.save(rating);
					} else {
						rating.setRating(newRating);
						template.update(rating);
					}
				}

				if (ratings.containsKey(person)) {
					System.out.println("Put player's rating: " + newRating);
					ratings.put(person, newRating);
				}
				fireRatingChangedEvent(person, board, oldRating, newRating);
			}
		} finally {
			ratingsLock.unlock();
		}
	}

	protected void fireRatingChangedEvent(Personality p, GameBoard board, short oldRating, short newRating) {
		for (PlayerRatingListener ratingListener : ratingListeners) {
			ratingListener.playerRatingChanged(p, board, oldRating, newRating);
		}
	}

	public void setRatingSystem(RatingSystem ratingSystem) {
		this.ratingSystem = ratingSystem;
	}

	public void setRoomsManager(RoomsManager roomsManager) {
		if (this.roomsManager != null) {
			for (RoomManager manager : this.roomsManager.getRoomManagers()) {
				final BoardManager boardManager = manager.getBoardManager();
				if (boardManager != null) {
					boardManager.removeBoardStateListener(boardStateListener);
				}
			}
		}

		this.roomsManager = roomsManager;

		if (this.roomsManager != null) {
			for (RoomManager manager : this.roomsManager.getRoomManagers()) {
				manager.getBoardManager().addBoardStateListener(boardStateListener);
			}
		}
	}

	private class TheBoardStateListener implements BoardStateListener {
		private TheBoardStateListener() {
		}

		@Override
		public void gameStarted(GameBoard board) {
		}

		@Override
		public <S extends GameSettings, P extends GamePlayerHand> void gameMoveDone(GameBoard<S, P> board, GameMove move) {
		}

		@Override
		public <S extends GameSettings, P extends GamePlayerHand> void gameFinished(GameBoard<S, P> board, GameResolution resolution, Collection<P> wonPlayers) {
			final Collection<P> playersHands = board.getPlayersHands();
			final GamePlayerHand[] hands = playersHands.toArray(new GamePlayerHand[playersHands.size()]);

			final short[] points = new short[hands.length];
			final short[] oldRatings = new short[hands.length];
			for (int i = 0; i < hands.length; i++) {
				final GamePlayerHand hand = hands[i];
				points[i] = hand.getPoints();
				oldRatings[i] = getRating(Personality.person(hand.getPlayerId()));
			}

			final short[] newRatings;
			if (board.isRatedGame()) {
				newRatings = ratingSystem.calculateRatings(oldRatings, points);
			} else {
				newRatings = oldRatings.clone(); // if game is not rated - no changes.
			}
			for (int i = 0; i < hands.length; i++) {
				final GamePlayerHand hand = hands[i];
				processRatingChange(hand.getPlayerId(), board, oldRatings[i], newRatings[i], hand.getPoints());
			}
		}
	}
}
