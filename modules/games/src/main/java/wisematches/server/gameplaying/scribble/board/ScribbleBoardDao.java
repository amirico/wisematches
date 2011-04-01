package wisematches.server.gameplaying.scribble.board;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import wisematches.server.gameplaying.board.GameResolution;
import wisematches.server.gameplaying.room.search.ExpiringBoard;
import wisematches.server.personality.Personality;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleBoardDao extends HibernateDaoSupport {
	public ScribbleBoardDao() {
	}

	public ScribbleBoard getScribbleBoard(long boardId) {
		final HibernateTemplate template = getHibernateTemplate();
		return template.get(ScribbleBoard.class, boardId);
	}

	public void saveScribbleBoard(ScribbleBoard scribbleBoard) {
		final HibernateTemplate template = getHibernateTemplate();

		// We call merge object because original can't be associated with another session
		if (scribbleBoard.getBoardId() == 0) {
			template.persist(scribbleBoard);
		} else {
			if (scribbleBoard.getGameMoves().isEmpty()) { // If no one move - it's possible that players list changed.
				// Persist all unsaved. In other case merge brakes transient filed "tiles".
				final List<ScribblePlayerHand> playersHands = scribbleBoard.getPlayersHands();
				for (ScribblePlayerHand hand : playersHands) {
					if (hand.getId() == 0) {
						template.persist(hand);
					}
				}
			}
			template.merge(scribbleBoard);
		}
		template.flush();
	}

	@SuppressWarnings("unchecked")
	public Collection<Long> getActiveBoards(Personality player) {
		final HibernateTemplate template = getHibernateTemplate();
		return (List<Long>) template.find("select board.boardId from " + ScribbleBoard.class.getName() +
				" board join board.playersIterator.playerHands hands where board.gameResolution is null and hands.playerId = ?", player.getId());
	}

	@SuppressWarnings("unchecked")
	public Collection<ExpiringBoard> findExpiringBoards() {
		final HibernateTemplate template = getHibernateTemplate();
		return template.find("select new " + ExpiringBoard.class.getName() + "(board.boardId, board.gameSettings.daysPerMove, board.lastMoveTime) from " +
				ScribbleBoard.class.getName() + " board where board.gameResolution is null");
	}
/*

	@SuppressWarnings("unchecked")
	public RatedBoardsInfo getRatedBoards(long playerId, Date startDate, Date endDate) {
		final HibernateTemplate template = getHibernateTemplate();

		final StringBuilder query = new StringBuilder();
		query.append("select board.boardId, board.finishedDate, hands.previousRating + hands.ratingDelta from ");
		query.append(ScribbleBoard.class.getName());
		query.append(" board join board.playersIterator.playerHands hands");
		query.append(" where (board.gameState = ? or board.gameState = ? or board.gameState = ?)");
		query.append(" and hands.playerId = ?");
		if (startDate != null) {
			query.append(" and board.finishedDate > ").append(startDate.getTime());
		}
		if (endDate != null) {
			query.append(" and board.finishedDate < ").append(endDate.getTime());
		}

		@SuppressWarnings("unchecked")
		final List<Object[]> list = template.find(query.toString(),
				new Object[]{
						GameState.FINISHED, GameState.DREW, GameState.INTERRUPTED, playerId
				});

		final long boardIds[] = new long[list.size()];
		final Date time[] = new Date[list.size()];
		final int rating[] = new int[list.size()];

		for (int i = 0; i < list.size(); i++) {
			final Object[] values = list.get(i);
			boardIds[i] = (Long) values[0];
			time[i] = (Date) values[1];
			rating[i] = (Integer) values[2];
		}
		return new RatedBoardsInfo(boardIds, time, rating);
	}
*/

	public int getGamesCount(EnumSet<GameResolution> states) {
		if (states != null && states.size() == 0) {
			return 0;
		}

		final HibernateTemplate template = getHibernateTemplate();

		final StringBuilder query = new StringBuilder();
		query.append("select count(*) from ");
		query.append(ScribbleBoard.class.getName());
		query.append(" board ");

		if (states == null) {
			query.append(" where board.gameResolution is null ");
			return ((Number) template.find(query.toString()).get(0)).intValue();
		} else {
			int index = 0;
			final Object[] values = new Object[states.size()];
			query.append(" where board.gameResolution in ( ");
			boolean first = true;
			for (GameResolution state : states) {
				if (!first) {
					query.append(", ");
				}
				query.append("?");
				first = false;
				values[index++] = state;
			}
			query.append(")");
			return ((Number) template.find(query.toString(), values).get(0)).intValue();
		}
	}
}
