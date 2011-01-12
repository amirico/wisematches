package wisematches.server.games.scribble.board;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import wisematches.kernel.player.Player;
import wisematches.server.games.board.GameState;
import wisematches.server.games.room.ExpiringBoardInfo;
import wisematches.server.games.room.RatedBoardsInfo;

import java.util.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleBoardDao extends HibernateDaoSupport {
	public ScribbleBoardDao() {
	}

	public ScribbleBoard getScribbleBoard(long boardId) {
		final HibernateTemplate template = getHibernateTemplate();
		return (ScribbleBoard) template.get(ScribbleBoard.class, boardId);
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

	public Collection<Long> getWaitingBoards() {
		final HibernateTemplate template = getHibernateTemplate();
		return (List<Long>) template.find("select board.boardId from " + ScribbleBoard.class.getName() + " board where board.gameState = ?", GameState.WAITING);
	}

	public Collection<Long> getActiveBoards(Player player) {
		final HibernateTemplate template = getHibernateTemplate();
		return (List<Long>) template.find("select board.boardId from " + ScribbleBoard.class.getName() +
				" board join board.playersIterator.playerHands hands where (board.gameState = ? or board.gameState = ?) and hands.playerId = ?",
				new Object[]{
						GameState.IN_PROGRESS,
						GameState.WAITING,
						player.getId()
				});
	}

	public Collection<ScribbleBoard> findBoards(EnumSet<GameState> statuses, long fromDate, long toDate) {
		final HibernateTemplate template = getHibernateTemplate();

		final List<Object> values = new ArrayList<Object>();

		final StringBuilder query = new StringBuilder();
		query.append("from ");
		query.append(ScribbleBoard.class.getName());
		query.append(" board ");

		values.add(fromDate);
		query.append(" and board.finishedDate >= ?");

		values.add(toDate);
		query.append(" and  board.finishedDate <= ?");

		if (statuses != null && statuses.size() != 0) {
			query.append(" and  board.gameState in ( ");
			boolean first = true;
			for (GameState statuse : statuses) {
				if (!first) {
					query.append(", ");
				}
				query.append("?");
				first = false;
				values.add(statuse);
			}
			query.append(")");
		}
		final String s = query.toString().replaceFirst(" and ", " where ");
		return template.find(s, values.toArray());
	}

	public Collection<ExpiringBoardInfo> findExpiringBoards() {
		final HibernateTemplate template = getHibernateTemplate();
		return template.find("select new " + ExpiringBoardInfo.class.getName() + "(board.boardId, board.gameSettings.daysPerMove, board.lastMoveTime) from " +
				ScribbleBoard.class.getName() + " board where board.gameState = ?", new Object[]{GameState.IN_PROGRESS}
		);
	}

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
						GameState.FINISHED, GameState.DRAW, GameState.INTERRUPTED, playerId
				});

		final long boardIds[] = new long[list.size()];
		final long time[] = new long[list.size()];
		final int rating[] = new int[list.size()];

		for (int i = 0; i < list.size(); i++) {
			final Object[] values = list.get(i);
			boardIds[i] = (Long) values[0];
			time[i] = (Long) values[1];
			rating[i] = (Integer) values[2];
		}
		return new RatedBoardsInfo(boardIds, time, rating);
	}

	public int getGamesCount(EnumSet<GameState> states) {
		if (states == null || states.size() == 0) {
			return 0;
		}

		final HibernateTemplate template = getHibernateTemplate();

		final StringBuilder query = new StringBuilder();
		query.append("select count(*) from ");
		query.append(ScribbleBoard.class.getName());
		query.append(" board ");

		int index = 0;
		final Object[] values = new Object[states.size()];
		query.append(" where board.gameState in ( ");
		boolean first = true;
		for (GameState state : states) {
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
