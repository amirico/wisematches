package wisematches.playground.history;

import wisematches.database.Order;
import wisematches.database.Range;
import wisematches.personality.Personality;

import java.util.List;

/**
 * {@code GameHistoryManager} provides information about finished games.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameHistoryManager<H extends GameHistory> {
	/**
	 * Returns number of finished games.
	 *
	 * @param personality the person who's games should be processed
	 * @return number of finished games.
	 */
	int getFinishedGamesCount(Personality personality);

	/**
	 * @param personality a person who's games should be returned
	 * @param range	   values range
	 * @param orders	  orders
	 * @return list of all finished games.
	 */
	List<H> getFinishedGames(Personality personality, Range range, Order... orders);

	/**
	 * @param personality a person who's games should be returned
	 * @param range	   values range
	 * @param orders	  orders
	 * @return list of all finished games.
	 */
	List<Long> getPlayedFormerly(Personality personality, Range range, Order... orders);
}
