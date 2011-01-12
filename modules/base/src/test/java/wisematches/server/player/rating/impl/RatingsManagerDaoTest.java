package wisematches.server.player.rating.impl;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import wisematches.kernel.player.Player;
import wisematches.server.core.account.AccountManager;
import wisematches.server.core.account.PlayerManager;
import wisematches.server.core.account.impl.PlayerImpl;
import wisematches.server.player.rating.RatingsManager;

import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RatingsManagerDaoTest extends AbstractTransactionalDataSourceSpringContextTests {
	private PlayerManager playerManager;
	private AccountManager accountManager;
	private RatingsManagerDao ratingsManagerDao;

	private final List<Player> players = new ArrayList<Player>();

	private static final int RATING_DELTA = 100;
	private static final int START_RATING = 10000;

	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();	//To change body of overridden methods use File | Settings | File Templates.

		int playersCount = 10;

		for (int i = 0; i < playersCount; i++) {
			final Player player = accountManager.createPlayer("TEST_RATING_PLAYER_" + i, "p", "TEST_RATING_EMAIL_" + i + "@test.ru");
			players.add(player);

			player.setRating(START_RATING + i * RATING_DELTA);
			playerManager.updatePlayer(player);
		}
	}

	protected String[] getConfigLocations() {
		return new String[]{"classpath:/config/test-server-config.xml"};
	}

	@Override
	protected void onTearDown() throws Exception {
		for (Player player : players) {
			accountManager.deletePlayer(player);
		}
		super.onTearDown();
	}

	public void testGetPlayersRating() {
		final int playersCount = 3;

		final int count = (int) ratingsManagerDao.getPlayersCount();
		int pages = count / playersCount;
		int lastPageCount = (count % playersCount);

		int lastRating = 0;
		for (int i = 0; i < pages; i++) {
			final Collection<Player> p = ratingsManagerDao.getPlayersRating(playersCount * i, playersCount, RatingsManager.SortType.ASC);
			assertEquals(playersCount, p.size());
			for (Player player : p) {
				assertTrue(player.getRating() >= lastRating);
				lastRating = player.getRating();
			}
		}

		if (lastPageCount != 0) {
			final Collection<Player> p = ratingsManagerDao.getPlayersRating(playersCount * pages, playersCount, RatingsManager.SortType.ASC);
			assertEquals(lastPageCount, p.size());
			for (Player player : p) {
				assertTrue(player.getRating() >= lastRating);
				lastRating = player.getRating();
			}
		}

		final List<Player> first = ratingsManagerDao.getPlayersRating(0, playersCount, RatingsManager.SortType.ASC);
		final List<Player> last = ratingsManagerDao.getPlayersRating(count - playersCount, playersCount, RatingsManager.SortType.DESC);
		Collections.reverse(last); //We need get a reverse order
		assertEquals(first, last);
	}

	public void testGetPlayersCount() {
		final long count = ratingsManagerDao.getPlayersCount();

		final Class<PlayerImpl> playerClass = PlayerImpl.class;
		final Table annotation = playerClass.getAnnotation(Table.class);
		assertEquals(countRowsInTable(annotation.name()), count);
	}

	public void testGetPlayerPosition() {
		final int count = (int) ratingsManagerDao.getPlayersCount();
		final List<Player> list = ratingsManagerDao.getPlayersRating(0, count, RatingsManager.SortType.DESC);

		final Player player = players.get(3);
		final int index = list.indexOf(player);
		assertEquals(index + 1, ratingsManagerDao.getPlayerPosition(player.getId()));

		assertEquals(0, ratingsManagerDao.getPlayerPosition(0));
	}

	public void setRatingsManagerDao(RatingsManagerDao ratingsManagerDao) {
		this.ratingsManagerDao = ratingsManagerDao;
	}

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}
}
