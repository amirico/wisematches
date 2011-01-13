package wisematches.server.player.statistic.impl;

import org.hibernate.SessionFactory;
import org.hibernate.persister.collection.AbstractCollectionPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import wisematches.server.player.statistic.PlayerRatingInfo;
import wisematches.server.player.statistic.PlayerStatistic;
import wisematches.server.player.statistic.PlayerStatisticListener;
import wisematches.server.player.statistic.PlayerStatisticsManager;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.easymock.EasyMock.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class HibernateStatisticsManagerTest extends AbstractTransactionalDataSourceSpringContextTests {
	private SessionFactory sessionFactory;
	private PlayerStatisticsManager playerStatisticsManager;

	protected String[] getConfigLocations() {
		return new String[]{"classpath:/config/test-server-base-config.xml"};
	}

	@Override
	protected void onTearDownInTransaction() throws Exception {
		jdbcTemplate.update("delete from stats_info where playerId = 1");
		jdbcTemplate.update("delete from stats_info where playerId = 2");

		sessionFactory.getCurrentSession().clear();

		//Clear hibernate second level cache
		@SuppressWarnings("unchecked")
		final Map<String, EntityPersister> classMetadata = sessionFactory.getAllClassMetadata();
		for (EntityPersister ep : classMetadata.values()) {
			if (ep.hasCache()) {
				sessionFactory.evictEntity(ep.getCache().getRegionName());
			}
		}

		@SuppressWarnings("unchecked")
		final Map<String, AbstractCollectionPersister> collMetadata = sessionFactory.getAllCollectionMetadata();
		for (AbstractCollectionPersister acp : collMetadata.values()) {
			if (acp.hasCache()) {
				sessionFactory.evictCollection(acp.getCache().getRegionName());
			}
		}
	}

	public void test_lockUnlock() throws InterruptedException {
		final ExecutorService executorService = Executors.newFixedThreadPool(2);

		playerStatisticsManager.lockPlayerStatistic(1L);
		final Future<PlayerStatistic> future = executorService.submit(new Callable<PlayerStatistic>() {
			public PlayerStatistic call() throws Exception {
				final TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
				try {
					final PlayerStatistic statistic = playerStatisticsManager.getPlayerStatistic(1L);
					transactionManager.commit(status);
					return statistic;
				} catch (Exception th) {
					transactionManager.rollback(status);
					th.printStackTrace();
					throw th;
				}
			}
		});
		final Future<PlayerStatistic> future2 = executorService.submit(new Callable<PlayerStatistic>() {
			public PlayerStatistic call() throws Exception {
				final TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
				try {
					final PlayerStatistic statistic = playerStatisticsManager.getPlayerStatistic(2L);
					transactionManager.commit(status);
					return statistic;
				} catch (Exception th) {
					transactionManager.rollback(status);
					th.printStackTrace();
					throw th;
				}
			}
		});
		Thread.sleep(1000);
		assertFalse(future.isDone());
		assertTrue(future2.isDone());

		final PlayerStatistic statistic = playerStatisticsManager.getPlayerStatistic(1L);
		assertNotNull(statistic);
		playerStatisticsManager.unlockPlayerStatistic(1L);

		Thread.sleep(1000);
		assertTrue(future.isDone());
	}

	public void test_updateStatistic() {
		final PlayerStatistic statistic = playerStatisticsManager.getPlayerStatistic(1);
		assertNotNull(statistic);

		long time = System.currentTimeMillis();

		final PlayerStatisticListener listener = createStrictMock(PlayerStatisticListener.class);
		listener.playerStatisticUpdated(1, statistic);
		expectLastCall().times(5);
		replay(listener);

		playerStatisticsManager.addPlayerStatisticListener(listener);

		statistic.setAverageTurnTime(1);
		statistic.setDrawGames(2);
		statistic.setLastMoveTime(3);
		statistic.setLostGames(4);
		statistic.setTimeouts(5);
		statistic.setTurnsCount(6);
		statistic.setWonGames(7);
		statistic.setActiveGames(8);
		playerStatisticsManager.updatePlayerStatistic(statistic);

		final PlayerRatingInfo ri1 = statistic.getAllGamesRaingInfo();
		ri1.setHighestRating(1);
		ri1.setLowestRating(2);
		ri1.setAverageMovesPerGame(3);
		ri1.setAverageOpponentRating(4);
		ri1.setAverageRating(5);
		ri1.setHighestWonOpponentRating(6);
		ri1.setHighestWonOpponentId(7);
		ri1.setLowestLostOpponentRating(8);
		ri1.setLowestLostOpponentId(9);
		playerStatisticsManager.updatePlayerStatistic(statistic);

		final PlayerRatingInfo ri2 = statistic.getNinetyDaysRaingInfo();
		ri2.setHighestRating(10);
		ri2.setLowestRating(20);
		ri2.setAverageMovesPerGame(30);
		ri2.setAverageOpponentRating(40);
		ri2.setAverageRating(50);
		ri2.setHighestWonOpponentRating(60);
		ri2.setHighestWonOpponentId(70);
		ri2.setLowestLostOpponentRating(80);
		ri2.setLowestLostOpponentId(90);
		playerStatisticsManager.updatePlayerStatistic(statistic);

		final PlayerRatingInfo ri3 = statistic.getThirtyDaysRaingInfo();
		ri3.setHighestRating(100);
		ri3.setLowestRating(200);
		ri3.setAverageMovesPerGame(300);
		ri3.setAverageOpponentRating(400);
		ri3.setAverageRating(500);
		ri3.setHighestWonOpponentRating(600);
		ri3.setHighestWonOpponentId(700);
		ri3.setLowestLostOpponentRating(800);
		ri3.setLowestLostOpponentId(900);
		playerStatisticsManager.updatePlayerStatistic(statistic);

		final PlayerRatingInfo ri4 = statistic.getYearRaingInfo();
		ri4.setHighestRating(1000);
		ri4.setLowestRating(2000);
		ri4.setAverageMovesPerGame(3000);
		ri4.setAverageOpponentRating(4000);
		ri4.setAverageRating(5000);
		ri4.setHighestWonOpponentRating(6000);
		ri4.setHighestWonOpponentId(7000);
		ri4.setLowestLostOpponentRating(8000);
		ri4.setLowestLostOpponentId(9000);
		playerStatisticsManager.updatePlayerStatistic(statistic);

		sessionFactory.getCurrentSession().clear();

		final PlayerStatistic s = playerStatisticsManager.getPlayerStatistic(1);
		assertEquals(1, s.getAverageTurnTime());
		assertEquals(2, s.getDrawGames());
		assertEquals(3, s.getLastMoveTime());
		assertEquals(4, s.getLostGames());
		assertEquals(5, s.getTimeouts());
		assertEquals(6, s.getTurnsCount());
		assertEquals(7, s.getWonGames());
		assertEquals(8, s.getActiveGames());
		assertTrue(s.getUpdateTime() >= time);

		final PlayerRatingInfo sri1 = s.getAllGamesRaingInfo();
		assertEquals(1, sri1.getHighestRating());
		assertEquals(2, sri1.getLowestRating());
		assertEquals(3, sri1.getAverageMovesPerGame());
		assertEquals(4, sri1.getAverageOpponentRating());
		assertEquals(5, sri1.getAverageRating());
		assertEquals(6, sri1.getHighestWonOpponentRating());
		assertEquals(7, sri1.getHighestWonOpponentId());
		assertEquals(8, sri1.getLowestLostOpponentRating());
		assertEquals(9, sri1.getLowestLostOpponentId());

		final PlayerRatingInfo sri2 = s.getNinetyDaysRaingInfo();
		assertEquals(10, sri2.getHighestRating());
		assertEquals(20, sri2.getLowestRating());
		assertEquals(30, sri2.getAverageMovesPerGame());
		assertEquals(40, sri2.getAverageOpponentRating());
		assertEquals(50, sri2.getAverageRating());
		assertEquals(60, sri2.getHighestWonOpponentRating());
		assertEquals(70, sri2.getHighestWonOpponentId());
		assertEquals(80, sri2.getLowestLostOpponentRating());
		assertEquals(90, sri2.getLowestLostOpponentId());

		final PlayerRatingInfo sri3 = s.getThirtyDaysRaingInfo();
		assertEquals(100, sri3.getHighestRating());
		assertEquals(200, sri3.getLowestRating());
		assertEquals(300, sri3.getAverageMovesPerGame());
		assertEquals(400, sri3.getAverageOpponentRating());
		assertEquals(500, sri3.getAverageRating());
		assertEquals(600, sri3.getHighestWonOpponentRating());
		assertEquals(700, sri3.getHighestWonOpponentId());
		assertEquals(800, sri3.getLowestLostOpponentRating());
		assertEquals(900, sri3.getLowestLostOpponentId());

		final PlayerRatingInfo sri4 = statistic.getYearRaingInfo();
		assertEquals(1000, sri4.getHighestRating());
		assertEquals(2000, sri4.getLowestRating());
		assertEquals(3000, sri4.getAverageMovesPerGame());
		assertEquals(4000, sri4.getAverageOpponentRating());
		assertEquals(5000, sri4.getAverageRating());
		assertEquals(6000, sri4.getHighestWonOpponentRating());
		assertEquals(7000, sri4.getHighestWonOpponentId());
		assertEquals(8000, sri4.getLowestLostOpponentRating());
		assertEquals(9000, sri4.getLowestLostOpponentId());

		verify(listener);
	}

	public void setPlayerStatisticsManager(PlayerStatisticsManager playerStatisticsManager) {
		this.playerStatisticsManager = playerStatisticsManager;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
