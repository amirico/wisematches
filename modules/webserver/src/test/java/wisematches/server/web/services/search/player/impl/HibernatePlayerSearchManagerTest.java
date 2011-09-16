package wisematches.server.web.services.search.player.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.server.web.services.search.player.PlayerInfoBean;
import wisematches.server.web.services.search.player.PlayerSearchArea;
import wisematches.server.web.services.search.player.PlayerSearchManager;

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
		"classpath:/config/scribble-junit-config.xml",
		"classpath:/config/application-settings.xml",
		"classpath:/config/server-web-junit-config.xml"
})
public class HibernatePlayerSearchManagerTest {
	@Autowired
	private PlayerSearchManager searchManager;

	public HibernatePlayerSearchManagerTest() {
	}

	@Test
	public void testFriends() {
		int playersCount = searchManager.getPlayersCount(Personality.person(1029), PlayerSearchArea.FRIENDS, null);
		System.out.println(playersCount);

		List<PlayerInfoBean> playerBeans = searchManager.getPlayerBeans(Personality.person(1029), PlayerSearchArea.FRIENDS, null, null);
		System.out.println(playerBeans);
	}

	@Test
	public void testFormerly() {
		int playersCount = searchManager.getPlayersCount(Personality.person(1029), PlayerSearchArea.FORMERLY, null);
		System.out.println(playersCount);

		List<PlayerInfoBean> playerBeans = searchManager.getPlayerBeans(Personality.person(1029), PlayerSearchArea.FORMERLY, null, null);
		System.out.println(playerBeans);
	}

	@Test
	public void testPlayers() {
		int playersCount = searchManager.getPlayersCount(Personality.person(1029), PlayerSearchArea.PLAYERS, null);
		System.out.println(playersCount);

		List<PlayerInfoBean> playerBeans = searchManager.getPlayerBeans(Personality.person(1029), PlayerSearchArea.PLAYERS, null, null);
		System.out.println(playerBeans);
	}
}
