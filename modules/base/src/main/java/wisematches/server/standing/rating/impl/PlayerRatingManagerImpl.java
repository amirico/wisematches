package wisematches.server.standing.rating.impl;

import wisematches.server.gameplaying.board.GameBoard;
import wisematches.server.player.Player;
import wisematches.server.standing.rating.PlayerRatingListener;
import wisematches.server.standing.rating.PlayerRatingManager;
import wisematches.server.standing.rating.RatingChange;
import wisematches.server.standing.rating.RatingHistory;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerRatingManagerImpl implements PlayerRatingManager {
	private final Map<Player, Short> ratings = new WeakHashMap<Player, Short>();

	private PlayerRatingsDao playerRatingsDao;

	@Override
	public void addRatingsChangeListener(PlayerRatingListener l) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void removeRatingsChangeListener(PlayerRatingListener l) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public short getRating(Player player) {
		Short rating = ratings.get(player);
		if (rating == null) {
			rating = playerRatingsDao.getRating(player.getId());
			ratings.put(player, rating);
		}
		return rating;
	}

	@Override
	public long getPosition(Player player) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public RatingChange getRatingChange(Player player, GameBoard board) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public RatingChange[] getBoardRatings(GameBoard board) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public RatingHistory getRatingHistory(Player player) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}
}
