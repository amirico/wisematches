package wisematches.playground.scribble.player;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Player;
import wisematches.core.search.Order;
import wisematches.core.search.Orders;
import wisematches.server.services.MockPlayer;
import wisematches.server.services.players.PlayerEntityBean;
import wisematches.server.services.players.PlayerSearchArea;
import wisematches.server.services.players.ScribblePlayerSearchManager;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml",
		"classpath:/config/personality-config.xml",
		"classpath:/config/playground-config.xml",
		"classpath:/config/scribble-config.xml"
})
public class ScribblePlayerSearchManagerTest {
	@Autowired
	private ScribblePlayerSearchManager playerSearchManager;

	private final Player player = new MockPlayer(1029);

	public ScribblePlayerSearchManagerTest() {
	}

	@Test
	public void testFriends() {
		int playersCount = playerSearchManager.getTotalCount(player, PlayerSearchArea.FRIENDS);
		System.out.println(playersCount);

		List<PlayerEntityBean> playerBeans = playerSearchManager.searchEntities(player, PlayerSearchArea.FRIENDS, null, null, null);
		System.out.println(playerBeans);
	}

	@Test
	public void testFormerly() {
		int playersCount = playerSearchManager.getTotalCount(player, PlayerSearchArea.FORMERLY);
		System.out.println(playersCount);

		List<PlayerEntityBean> playerBeans = playerSearchManager.searchEntities(player, PlayerSearchArea.FORMERLY, null, null, null);
		System.out.println(playerBeans);
	}

	@Test
	public void testPlayers() {
		int playersCount = playerSearchManager.getTotalCount(player, PlayerSearchArea.PLAYERS);
		System.out.println(playersCount);

		List<PlayerEntityBean> playerBeans = playerSearchManager.searchEntities(player, PlayerSearchArea.PLAYERS, null, Orders.of(Order.desc("nickname"), Order.asc("language")), null);
		System.out.println(playerBeans);
	}
}
