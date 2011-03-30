package wisematches.server.standing.rating.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.PlatformTransactionManager;
import wisematches.server.gameplaying.board.*;
import wisematches.server.gameplaying.room.Room;
import wisematches.server.gameplaying.room.RoomManager;
import wisematches.server.gameplaying.room.RoomsManager;
import wisematches.server.gameplaying.room.board.BoardStateListener;
import wisematches.server.player.Player;
import wisematches.server.player.PlayerManager;
import wisematches.server.standing.rating.PlayerRatingEvent;
import wisematches.server.standing.rating.PlayerRatingListener;
import wisematches.server.standing.rating.RatingSystem;
import wisematches.server.standing.rating.impl.systems.ELORatingSystem;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * This manager calculates rating and updates associated objects. It has a listeners that is invoked when
 * player's rating is updated to notify all interested clients.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RatingsCalculationCenter {
	private RoomsManager roomsManager;
	private PlayerManager playerManager;

	private RatingSystem ratingSystem = new ELORatingSystem();
	private PlatformTransactionManager transactionManager;

	private final Collection<PlayerRatingListener> listeners = new CopyOnWriteArraySet<PlayerRatingListener>();
	private static final Log log = LogFactory.getLog(RatingsCalculationCenter.class);

	public RatingsCalculationCenter() {
	}

	public void addRatingsChangeListener(PlayerRatingListener l) {
		listeners.add(l);
	}

	public void removeRatingsChangeListener(PlayerRatingListener l) {
		listeners.remove(l);
	}

	private void updatePlayerRatings(final Room room, final GameBoard board, final GamePlayerHand interrupterPlayer) {
		if (!board.isRatedGame()) { // If game is not rated just ignore it
			return;
		}

		@SuppressWarnings("unchecked")
		final Collection<GamePlayerHand> hands = board.getPlayersHands();

		int index = 0;
		final int[] points = new int[hands.size()];
		final Player[] players = new Player[hands.size()];
		for (GamePlayerHand hand : hands) {
			points[index] = (interrupterPlayer == hand ? 0 : hand.getPoints());
			players[index] = playerManager.getPlayer(hand.getPlayerId());
			index++;
		}

		final int[] oldRatings = new int[players.length];
		final int[] newRatings = ratingSystem.calculateRatings(players, points);

//		final TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
		try {
			for (int i = 0; i < players.length; i++) {
				final Player player = players[i];
//				final GamePlayerHand hand = hands.get(i);

				final int oldRating = player.getRating();
				int newRating = newRatings[i];
				if (newRating < 0) {
					newRating = 0;
				}
				final int ratingDelta = newRating - oldRating;

				oldRatings[i] = oldRating;

				// TODO: commented
//				player.setRating(newRating);
//				hand.updateRating(oldRating, ratingDelta);

				// TODO: commented
//				playerManager.updatePlayer(player);
			}
//			updateGameBoard(room, board);
//			transactionManager.commit(status);
		} catch (Throwable th) {
			log.fatal("Player ratings can't be updated", th);
//			transactionManager.rollback(status);
		}

		for (int i = 0; i < players.length; i++) {
			firePlayerRatingChanged(players[i], board, oldRatings[i], newRatings[i]);
		}
	}
/*

	@SuppressWarnings("unchecked")
	private void updateGameBoard(Room room, GameBoard board) {
		final BoardManager boardManager = roomsManager.getBoardManager(room);
		try {
			boardManager.updateBoard(board);
		} catch (BoardUpdatingException e) {
			log.fatal("Game board can't be updated", e);
		}
	}
*/

	protected void firePlayerRatingChanged(Player player, GameBoard board, int oldRating, int newRating) {
		final PlayerRatingEvent e = new PlayerRatingEvent(player, board, oldRating, newRating);
		for (PlayerRatingListener listener : listeners) {
			listener.playerRaitingChanged(e);
		}
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public void setRoomsManager(RoomsManager roomsManager) {
		this.roomsManager = roomsManager;

		final Collection<RoomManager> collection = roomsManager.getRoomManagers();
		for (RoomManager roomManager : collection) {
			final Room type = roomManager.getRoomType();
			roomManager.getBoardManager().addBoardStateListener(new TheBoardStateListener(type));
		}
	}

	public void setRatingSystem(RatingSystem ratingSystem) {
		this.ratingSystem = ratingSystem;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	private final class TheBoardStateListener implements BoardStateListener {
		private final Room room;

		private TheBoardStateListener(Room room) {
			this.room = room;
		}

		@Override
		public void gameStarted(GameBoard board) {
		}

		@Override
		public void gameMoveDone(GameBoard board, GameMove move) {
		}

		@Override
		public <S extends GameSettings, P extends GamePlayerHand> void gameFinished(GameBoard<S, P> board, GameResolution resolution, Collection<P> wonPlayers) {
			updatePlayerRatings(room, board, null);
		}
	}
}
