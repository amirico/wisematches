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
import wisematches.core.search.Range;
import wisematches.server.services.relations.players.PlayerContext;
import wisematches.server.services.relations.players.PlayerEntityBean;
import wisematches.server.services.relations.players.PlayerRelationship;
import wisematches.server.services.relations.players.impl.HibernatePlayerSearchManager;

import java.util.Date;
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
public class HibernatePlayerSearchManagerTest {
	@Autowired
	private HibernatePlayerSearchManager playerSearchManager;

	private final Player player = new DefaultMember(1029, null, null, null, null, null);

	public HibernatePlayerSearchManagerTest() {
	}

	@Test
	public void testFriends() {
		int playersCount = playerSearchManager.getTotalCount(player, new PlayerContext(PlayerRelationship.FRIENDS));
		System.out.println(playersCount);

		List<PlayerEntityBean> playerBeans = playerSearchManager.searchEntities(player, new PlayerContext(PlayerRelationship.FRIENDS), null, null);
		System.out.println(playerBeans);
	}

	@Test
	public void testFormerly() {
		int playersCount = playerSearchManager.getTotalCount(player, new PlayerContext(PlayerRelationship.FORMERLY));
		System.out.println(playersCount);

		List<PlayerEntityBean> playerBeans = playerSearchManager.searchEntities(player, new PlayerContext(PlayerRelationship.FORMERLY), null, null);
		System.out.println(playerBeans);
	}

	@Test
	public void testPlayers() {
		int playersCount = playerSearchManager.getTotalCount(player, new PlayerContext(null, null));
		System.out.println(playersCount);

		List<PlayerEntityBean> playerBeans = playerSearchManager.searchEntities(player, new PlayerContext(null, null), Orders.of(Order.desc("player"), Order.asc("language")), null);
		System.out.println(playerBeans);
	}

	@Test
	public void testContext() {
		final PlayerContext context = new PlayerContext("asd", null, Range.limit(1200, 600), Range.limit(1, 10), Range.limit(0, 5), new Date(System.currentTimeMillis() - 86400000), 234123535125f);

		int playersCount = playerSearchManager.getTotalCount(player, context);
		System.out.println(playersCount);

		List<PlayerEntityBean> playerBeans = playerSearchManager.searchEntities(player, context, Orders.of(Order.desc("player"), Order.asc("language")), null);
		System.out.println(playerBeans);

	}
}
