package wisematches.server.player.rating.impl;

import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;
import wisematches.kernel.player.Player;
import wisematches.server.core.MockPlayer;
import wisematches.server.player.rating.PlayerRatingEvent;
import wisematches.server.player.rating.PlayerRatingListener;
import wisematches.server.player.rating.PlayerRatingsManager;
import wisematches.server.player.rating.TopPlayersListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RatingsManagerImplTest {
	private RatingsManagerImpl ratingsManager;

	private Player[] players;
	private TopPlayersListener topPlayersListener;
	private PlayerRatingListener playerRatingListener;

	private static final int TOP_COUNT = 5;

	@Before
	public void setUp() {
		players = new Player[50];
		int maxRating = 1000 + 100 * players.length;
		for (int i = 0; i < players.length; i++) {
			players[i] = new MockPlayer(i, maxRating - 100 * i);
		}

		RatingsCalculationCenter ratingsCalculationManager = createMock(RatingsCalculationCenter.class);
		ratingsCalculationManager.addRatingsChangeListener(isA(PlayerRatingListener.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			public Object answer() throws Throwable {
				playerRatingListener = (PlayerRatingListener) getCurrentArguments()[0];
				return null;
			}
		});
		replay(ratingsCalculationManager);

		ratingsManager = new RatingsManagerImpl();
		ratingsManager.setRatingsCalculationManager(ratingsCalculationManager);
		ratingsManager.setTopPlayersCount(TOP_COUNT);
	}

	@Test
	public void test_setRatingsManagerDao() {
		final List<Player> value = Arrays.asList(Arrays.copyOf(players, 5));

		RatingsManagerDao dao = createStrictMock(RatingsManagerDao.class);
		expect(dao.getPlayersRating(0, TOP_COUNT, PlayerRatingsManager.SortType.DESC)).andReturn(value);
		replay(dao);

		ratingsManager.setRatingsManagerDao(dao);
		assertArrayEquals(value.toArray(), ratingsManager.getTopRatedPlayers().toArray());

		verify(dao);
	}

	@Test
	public void test_getPlayerPosition() {
		RatingsManagerDao dao = createStrictMock(RatingsManagerDao.class);
		expect(dao.getPlayersRating(0, 5, PlayerRatingsManager.SortType.DESC)).andReturn(Collections.<Player>emptyList());
		expect(dao.getPlayerPosition(13L)).andReturn(456L);
		replay(dao);

		ratingsManager.setRatingsManagerDao(dao);
		assertEquals(456L, ratingsManager.getPlayerPosition(13L));
		verify(dao);
	}

	@Test
	public void test_getPlayersCount() {
		RatingsManagerDao dao = createStrictMock(RatingsManagerDao.class);
		expect(dao.getPlayersRating(0, 5, PlayerRatingsManager.SortType.DESC)).andReturn(Collections.<Player>emptyList());
		expect(dao.getPlayersCount()).andReturn(456L);
		replay(dao);

		ratingsManager.setRatingsManagerDao(dao);
		assertEquals(456L, ratingsManager.getPlayersCount());
		verify(dao);
	}

	@Test
	public void test_getPlayersRating() {
		@SuppressWarnings("unchecked")
		final List<Player> list = createNiceMock(List.class);

		RatingsManagerDao dao = createStrictMock(RatingsManagerDao.class);
		expect(dao.getPlayersRating(0, 5, PlayerRatingsManager.SortType.DESC)).andReturn(Collections.<Player>emptyList());
		expect(dao.getPlayersRating(1, 3, PlayerRatingsManager.SortType.DESC)).andReturn(list);
		replay(dao);

		ratingsManager.setRatingsManagerDao(dao);
		assertSame(list, ratingsManager.getPlayersRating(1, 3, PlayerRatingsManager.SortType.DESC));
		verify(dao);
	}

	@Test
	public void test_setTopPlayersCount() {
		final List<Player> value = Arrays.asList(Arrays.copyOf(players, TOP_COUNT));

		topPlayersListener = createStrictMock(TopPlayersListener.class);
		topPlayersListener.topPlayersChanged();
		topPlayersListener.topPlayersChanged();
		replay(topPlayersListener);

		ratingsManager.addTopPlayersListener(topPlayersListener);

		RatingsManagerDao dao = createStrictMock(RatingsManagerDao.class);
		expect(dao.getPlayersRating(0, TOP_COUNT, PlayerRatingsManager.SortType.DESC)).andReturn(value);
		replay(dao);

		ratingsManager.setRatingsManagerDao(dao);
		assertArrayEquals(Arrays.copyOf(players, TOP_COUNT), ratingsManager.getTopRatedPlayers().toArray());
		verify(dao);

		reset(dao);
		replay(dao);
		ratingsManager.setTopPlayersCount(3);
		assertArrayEquals(Arrays.copyOf(players, 3), ratingsManager.getTopRatedPlayers().toArray());
		verify(dao);

		reset(dao);
		expect(dao.getPlayersRating(3, 2, PlayerRatingsManager.SortType.DESC)).andReturn(Arrays.asList(Arrays.copyOfRange(players, 3, 5)));
		replay(dao);
		ratingsManager.setTopPlayersCount(5);
		assertArrayEquals(Arrays.copyOf(players, 5), ratingsManager.getTopRatedPlayers().toArray());
		verify(dao);

		verify(topPlayersListener);
	}

	@Test
	public void test_playerRatingChangedNotification() {
		final List<Player> value = Arrays.asList(Arrays.copyOf(players, 5));

		topPlayersListener = createStrictMock(TopPlayersListener.class);
		topPlayersListener.topPlayersChanged();
		expectLastCall().times(3);
		replay(topPlayersListener);

		ratingsManager.addTopPlayersListener(topPlayersListener);

		final RatingsManagerDao dao = createStrictMock(RatingsManagerDao.class);
		expect(dao.getPlayersRating(0, 5, PlayerRatingsManager.SortType.DESC)).andReturn(value);
		replay(dao);
		ratingsManager.setTopPlayersCount(5);
		ratingsManager.setRatingsManagerDao(dao);
		assertArrayEquals(Arrays.copyOf(players, 5), ratingsManager.getTopRatedPlayers().toArray());
		verify(dao);

		// Rating is out of lowest. Noting to do.
		players[49].setRating(0);
		playerRatingListener.playerRaitingChanged(new PlayerRatingEvent(players[49], null, 0, 0));
		assertArrayEquals(Arrays.copyOf(players, 5), ratingsManager.getTopRatedPlayers().toArray());

		// Unknown player's rating changed to hiest
		players[49].setRating(players[2].getRating() + 1);
		playerRatingListener.playerRaitingChanged(new PlayerRatingEvent(players[49], null, 0, players[49].getRating()));
		assertEquals(5, ratingsManager.getTopRatedPlayers().size());
		final Iterator<Player> iter = ratingsManager.getTopRatedPlayers().iterator();
		assertEquals(players[0], iter.next());
		assertEquals(players[1], iter.next());
		assertEquals(players[49], iter.next());
		assertEquals(players[2], iter.next());
		assertEquals(players[3], iter.next());

		// Known player's rating changed to hiest
		players[3].setRating(players[2].getRating() + 1);
		playerRatingListener.playerRaitingChanged(new PlayerRatingEvent(players[3], null, 0, players[3].getRating()));
		assertEquals(5, ratingsManager.getTopRatedPlayers().size());
		final Iterator<Player> iter2 = ratingsManager.getTopRatedPlayers().iterator();
		assertEquals(players[0], iter2.next());
		assertEquals(players[1], iter2.next());
		assertEquals(players[49], iter2.next());
		assertEquals(players[3], iter2.next());
		assertEquals(players[2], iter2.next());

		// Known player's rating changed to lowest
		reset(dao);
		expect(dao.getPlayersRating(4, 1, PlayerRatingsManager.SortType.DESC)).andReturn(Arrays.asList(players[4]));
		replay(dao);

		players[3].setRating(0);
		playerRatingListener.playerRaitingChanged(new PlayerRatingEvent(players[3], null, 0, players[3].getRating()));
		assertEquals(5, ratingsManager.getTopRatedPlayers().size());
		final Iterator<Player> iter3 = ratingsManager.getTopRatedPlayers().iterator();
		assertEquals(players[0], iter3.next());
		assertEquals(players[1], iter3.next());
		assertEquals(players[49], iter3.next());
		assertEquals(players[2], iter3.next());
		assertEquals(players[4], iter3.next());
		verify(dao);

		verify(topPlayersListener);
	}


	@Test
	public void test_updateTop5Ratings() {
		topPlayersListener = createStrictMock(TopPlayersListener.class);
		topPlayersListener.topPlayersChanged();
		replay(topPlayersListener);

		ratingsManager.addTopPlayersListener(topPlayersListener);

		final List<Player> value = Arrays.asList(Arrays.copyOf(players, 2));

		final RatingsManagerDao dao = createStrictMock(RatingsManagerDao.class);
		expect(dao.getPlayersRating(0, 5, PlayerRatingsManager.SortType.DESC)).andReturn(value);
		replay(dao);
		ratingsManager.setTopPlayersCount(5);
		ratingsManager.setRatingsManagerDao(dao);
		assertArrayEquals(Arrays.copyOf(players, 2), ratingsManager.getTopRatedPlayers().toArray());
		verify(dao);

		// Rating is out of lowest. Noting to do.
		players[1].setRating(48);
		playerRatingListener.playerRaitingChanged(new PlayerRatingEvent(players[1], null, 26, 48));

		final Object[] objects = ratingsManager.getTopRatedPlayers().toArray();
		assertArrayEquals(Arrays.copyOf(players, 2), objects);

		verify(topPlayersListener);
	}
}
