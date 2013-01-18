package wisematches.playground.scribble.tracking.impl;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Personality;
import wisematches.playground.GameMove;
import wisematches.playground.GameState;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleBoardManager;
import wisematches.playground.scribble.ScribbleMoveScore;
import wisematches.playground.scribble.tracking.ScribbleStatisticsEditor;
import wisematches.playground.scribble.tracking.ScribbleStatisticsTrapper;
import wisematches.playground.tracking.impl.PlayerTrackingCenterDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerStatisticValidator {
	private SessionFactory sessionFactory;
	private ScribbleBoardManager boardManager;
	private PlayerTrackingCenterDao trackingCenterDao;
	private ScribbleStatisticsTrapper statisticsTrapper;

	private static final Logger log = Logger.getLogger("wisematches.server.statistics.validator");

	public PlayerStatisticValidator() {
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void recalculateStatistics() {
		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery("select account.id from wisematches.personality.account.impl.HibernateAccountImpl as account");
		final List list = query.list();
		for (Object o : list) {
			Long pid = (Long) o;
			recalculateStatistics(Personality.person(pid));
		}
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void recalculateStatistics(Personality player) {
		log.info("Validate player statistic: " + player);

		final List<ScribbleBoard> scribbleBoards = new ArrayList<ScribbleBoard>();

		scribbleBoards.addAll(boardManager.searchEntities(player, GameState.ACTIVE, null, null, null));
		scribbleBoards.addAll(boardManager.searchEntities(player, GameState.FINISHED, null, null, null));
		scribbleBoards.addAll(boardManager.searchEntities(player, GameState.INTERRUPTED, null, null, null));

		Collections.sort(scribbleBoards, new Comparator<ScribbleBoard>() {
			@Override
			public int compare(ScribbleBoard o1, ScribbleBoard o2) {
				return (int) (o1.getBoardId() - o2.getBoardId());
			}
		});

		log.info("Found games for validation: " + scribbleBoards.size());

		final ScribbleStatisticsEditor editor = statisticsTrapper.createStatisticsEditor(player);
		for (ScribbleBoard board : scribbleBoards) {
			statisticsTrapper.trapGameStarted(editor);
			for (GameMove move : board.getGameMoves()) {
				if (move.getPlayerMove().getPlayerId() == player.getId()) {
					statisticsTrapper.trapGameMoveDone(board, move, new ScribbleMoveScore((short) move.getPoints(), false, null, null), editor);
				}
			}

			if (!board.isGameActive()) {
				statisticsTrapper.trapGameFinished(board, editor);
			}
		}
		trackingCenterDao.savePlayerStatistic(editor);

		log.info("Recalculation finished: " + editor);
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setBoardManager(ScribbleBoardManager boardManager) {
		this.boardManager = boardManager;
	}

	public void setTrackingCenterDao(PlayerTrackingCenterDao trackingCenterDao) {
		this.trackingCenterDao = trackingCenterDao;
	}

	public void setStatisticsTrapper(ScribbleStatisticsTrapper statisticsTrapper) {
		this.statisticsTrapper = statisticsTrapper;
	}
}
