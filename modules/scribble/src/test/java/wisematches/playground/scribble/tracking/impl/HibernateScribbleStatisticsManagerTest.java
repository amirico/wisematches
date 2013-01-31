package wisematches.playground.scribble.tracking.impl;

import org.easymock.Capture;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Personality;
import wisematches.core.Player;
import wisematches.core.personality.PlayerManager;
import wisematches.core.personality.player.Member;
import wisematches.core.personality.player.account.*;
import wisematches.playground.*;
import wisematches.playground.scribble.ExchangeMove;
import wisematches.playground.scribble.PassTurn;
import wisematches.playground.scribble.ScribblePlayerHand;
import wisematches.playground.scribble.tracking.ScribbleStatistics;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml",
		"classpath:/config/personality-config.xml",
		"classpath:/config/scribble-junit-config.xml"
})
public class HibernateScribbleStatisticsManagerTest {
	private Player player;
	private Account person;

	@Autowired
	private PlayerManager playerManager;

	@Autowired
	private AccountManager accountManager;

	@Autowired
	private SessionFactory sessionFactory;


	private BoardListener boardListener;
	private HibernateScribbleStatisticsManager scribbleStatisticsManager;

	public HibernateScribbleStatisticsManagerTest() {
	}

	@Before
	public void setUp() throws InadmissibleUsernameException, DuplicateAccountException {
		final Capture<BoardListener> listenerCapture = new Capture<>();

		GamePlayManager gamePlayManager = createMock(GamePlayManager.class);
		gamePlayManager.addBoardListener(capture(listenerCapture));
		replay(gamePlayManager);

		scribbleStatisticsManager = new HibernateScribbleStatisticsManager();
		scribbleStatisticsManager.setSessionFactory(sessionFactory);
		scribbleStatisticsManager.setGamePlayManager(gamePlayManager);

		final String uuid = UUID.randomUUID().toString();
		person = accountManager.createAccount(new AccountEditor(uuid + "@mock.wm", uuid, "AS").createAccount());
		player = new Member(person);

		boardListener = listenerCapture.getValue();
	}

	@After
	public void tearDown() throws UnknownAccountException {
		if (person != null) {
			accountManager.removeAccount(person);
		}
	}

	@Test
	public void test_playerStatistic() throws InterruptedException, InadmissibleUsernameException, DuplicateAccountException {
		final ScribbleStatistics statistic = scribbleStatisticsManager.getStatistic(player);
		assertNotNull(statistic);
		assertEquals(0, statistic.getActiveGames());
		assertEquals(0, statistic.getFinishedGames());
		assertEquals(0, statistic.getTurnsCount());
		assertEquals(0, statistic.getPassesCount());
		assertEquals(0, statistic.getExchangesCount());
		assertEquals(0, statistic.getWordsCount());
		assertEquals(0, statistic.getResigned());

		final GameBoard<?, ?> board = createMock(GameBoard.class);
		expect(board.getPlayers()).andReturn(Arrays.<Personality>asList(player));
		replay(board);

		boardListener.gameStarted(board);
		assertEquals(1, statistic.getActiveGames());
		assertEquals(0, statistic.getFinishedGames());
		assertEquals(0, statistic.getTurnsCount());
		assertEquals(0, statistic.getPassesCount());
		assertEquals(0, statistic.getExchangesCount());
		assertEquals(0, statistic.getWordsCount());
		assertEquals(0, statistic.getResigned());

		reset(board);
		expect(board.getGameMoves()).andReturn(Collections.<GameMove>emptyList());
		expect(board.getStartedTime()).andReturn(new Date());
		replay(board);
		boardListener.gameMoveDone(board, new PassTurn(player, 0, new Date()), null);
		assertEquals(1, statistic.getActiveGames());
		assertEquals(0, statistic.getFinishedGames());
		assertEquals(1, statistic.getTurnsCount());
		assertEquals(1, statistic.getPassesCount());
		assertEquals(0, statistic.getExchangesCount());
		assertEquals(0, statistic.getWordsCount());
		assertEquals(0, statistic.getResigned());

		reset(board);
		expect(board.getGameMoves()).andReturn(Collections.<GameMove>emptyList());
		expect(board.getStartedTime()).andReturn(new Date());
		replay(board);
		boardListener.gameMoveDone(board, new ExchangeMove(player, 0, new Date(), new int[]{}), null);
		assertEquals(1, statistic.getActiveGames());
		assertEquals(0, statistic.getFinishedGames());
		assertEquals(2, statistic.getTurnsCount());
		assertEquals(1, statistic.getPassesCount());
		assertEquals(1, statistic.getExchangesCount());
		assertEquals(0, statistic.getWordsCount());
		assertEquals(0, statistic.getResigned());

		reset(board);
		expect(board.getPlayers()).andReturn(Arrays.<Personality>asList(player));
		expect(board.getGameMoves()).andReturn(Collections.<GameMove>emptyList());
		expect(board.getPlayerTurn()).andReturn(player);
		expect(board.getResolution()).andReturn(GameResolution.RESIGNED);
		expect(board.isRated()).andReturn(true);
		expect(board.getPlayers()).andReturn(Arrays.<Personality>asList(player, player));
		expect(board.getWonPlayers()).andReturn(Arrays.<Personality>asList(player));
		expect(board.getPlayerHand(player)).andReturn(new ScribblePlayerHand(player));
		replay(board);
		boardListener.gameFinished(board, GameResolution.RESIGNED, null);
		assertEquals(0, statistic.getActiveGames());
		assertEquals(1, statistic.getFinishedGames());
		assertEquals(2, statistic.getTurnsCount());
		assertEquals(1, statistic.getPassesCount());
		assertEquals(1, statistic.getExchangesCount());
		assertEquals(0, statistic.getWordsCount());
		assertEquals(1, statistic.getResigned());
		assertEquals(1, statistic.getWins());
	}
}
