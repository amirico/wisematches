package wisematches.playground.scribble.history;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.database.Order;
import wisematches.database.Orders;
import wisematches.database.Range;
import wisematches.personality.Personality;
import wisematches.playground.GameResolution;

import java.util.List;

import static org.junit.Assert.assertEquals;

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
public class ScribbleHistorySearchManagerTest {
	@Autowired
	private ScribbleHistorySearchManager historySearchManager;

	public ScribbleHistorySearchManagerTest() {
	}

	@Test
	public void testName() throws Exception {
		final Personality player = Personality.person(1029);

		int finishedGamesCount = historySearchManager.getTotalCount(player, null);
		List<ScribbleHistoryEntity> historyEntity1 = historySearchManager.searchEntities(player, null, null, null, null);
		assertEquals(finishedGamesCount, historyEntity1.size());

		List<ScribbleHistoryEntity> historyEntity2 = historySearchManager.searchEntities(player, null, null, Orders.of(Order.asc("language")), Range.limit(4, 10));
		if (finishedGamesCount > 10) {
			assertEquals(10, historyEntity2.size());
		} else {
			assertEquals(finishedGamesCount, historyEntity2.size());
		}

		historySearchManager.searchEntities(player, GameResolution.FINISHED, null, null, null);
		historySearchManager.searchEntities(player, GameResolution.RESIGNED, null, null, null);
		historySearchManager.searchEntities(player, GameResolution.STALEMATE, null, null, null);
		historySearchManager.searchEntities(player, GameResolution.TIMEOUT, null, null, null);
	}
}
