package wisematches.server.services.relations;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Player;
import wisematches.core.personality.DefaultMember;
import wisematches.core.search.Order;
import wisematches.core.search.Orders;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/properties-config.xml",
		"classpath:/config/database-config.xml",
		"classpath:/config/personality-config.xml",
		"classpath:/config/playground-config.xml",
		"classpath:/config/scribble-config.xml",
})
public class ScribblePlayerSearchManagerTest {
	@Autowired
	private ScribblePlayerSearchManager playerSearchManager;

	private final Player player = new DefaultMember(1029, null, null, null, null, null);

	public ScribblePlayerSearchManagerTest() {
	}

	@Test
	public void testFriends() {
		int playersCount = playerSearchManager.getTotalCount(player, PlayerSearchArea.FRIENDS);
		System.out.println(playersCount);

		List<PlayerEntityBean> playerBeans = playerSearchManager.searchEntities(player, PlayerSearchArea.FRIENDS, null, null);
		System.out.println(playerBeans);
	}

	@Test
	public void testFormerly() {
		int playersCount = playerSearchManager.getTotalCount(player, PlayerSearchArea.FORMERLY);
		System.out.println(playersCount);

		List<PlayerEntityBean> playerBeans = playerSearchManager.searchEntities(player, PlayerSearchArea.FORMERLY, null, null);
		System.out.println(playerBeans);
	}

	@Test
	public void testPlayers() {
		int playersCount = playerSearchManager.getTotalCount(player, PlayerSearchArea.PLAYERS);
		System.out.println(playersCount);

		List<PlayerEntityBean> playerBeans = playerSearchManager.searchEntities(player, PlayerSearchArea.PLAYERS, Orders.of(Order.desc("player"), Order.asc("language")), null);
		System.out.println(playerBeans);
	}
}
