package wisematches.playground.scribble.search.player;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.database.Order;
import wisematches.personality.Personality;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml",
		"classpath:/config/accounts-config.xml",
		"classpath:/config/playground-config.xml",
		"classpath:/config/scribble-junit-config.xml"
})
public class ScribblePlayerSearchManagerTest {
	@Autowired
	private ScribblePlayerSearchManager playerSearchManager;

	public ScribblePlayerSearchManagerTest() {
	}

	@Test
	public void testFriends() {
		int playersCount = playerSearchManager.getTotalCount(Personality.person(1029), PlayerSearchArea.FRIENDS);
		System.out.println(playersCount);

		List<PlayerEntityBean> playerBeans = playerSearchManager.searchEntities(Personality.person(1029), PlayerSearchArea.FRIENDS, null, null, null);
		System.out.println(playerBeans);
	}

	@Test
	public void testFormerly() {
		int playersCount = playerSearchManager.getTotalCount(Personality.person(1029), PlayerSearchArea.FORMERLY);
		System.out.println(playersCount);

		List<PlayerEntityBean> playerBeans = playerSearchManager.searchEntities(Personality.person(1029), PlayerSearchArea.FORMERLY, null, null, null);
		System.out.println(playerBeans);
	}

	@Test
	public void testPlayers() {
		int playersCount = playerSearchManager.getTotalCount(Personality.person(1029), PlayerSearchArea.PLAYERS);
		System.out.println(playersCount);

		List<PlayerEntityBean> playerBeans = playerSearchManager.searchEntities(Personality.person(1029), PlayerSearchArea.PLAYERS, null, new Order[]{Order.desc("nickname"), Order.asc("language")}, null);
		System.out.println(playerBeans);
	}
}
