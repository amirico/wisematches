package wisematches.playground.scribble.tracking.impl;

/**
 * TODO: commented!
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Deprecated
public class PlayerStatisticValidator {
/*
	private SessionFactory sessionFactory;
	private ScribblePlayManager boardManager;
	private ScribbleStatisticsTrapper statisticsTrapper;

	private static final Log log = LogFactory.getLog("wisematches.server.statistics.validator");

	public PlayerStatisticValidator() {
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void recalculateStatistics() {
		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery("select account.id from HibernateAccount as account");
		final List list = query.list();
		for (Object o : list) {
			Long pid = (Long) o;
			recalculateStatistics(Personality.person(pid));
		}
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void recalculateStatistics(Personality player) {
		log.info("Validate player statistic: " + player);

		final List<ScribbleBoard> scribbleBoards = new ArrayList<>();

		scribbleBoards.addAll(boardManager.searchEntities(player, GameState.ACTIVE, null, null, null));
		scribbleBoards.addAll(boardManager.searchEntities(player, GameState.FINISHED, null, null, null));

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

			if (!board.isActive()) {
				statisticsTrapper.trapGameFinished(board, , editor, );
			}
		}
		trackingCenterDao.savePlayerStatistic(editor);

		log.info("Recalculation finished: " + editor);
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setBoardManager(ScribblePlayManager boardManager) {
		this.boardManager = boardManager;
	}

	public void setTrackingCenterDao(PlayerTrackingCenterDao trackingCenterDao) {
		this.trackingCenterDao = trackingCenterDao;
	}

	public void setStatisticsTrapper(ScribbleStatisticsTrapper statisticsTrapper) {
		this.statisticsTrapper = statisticsTrapper;
	}
*/
}
