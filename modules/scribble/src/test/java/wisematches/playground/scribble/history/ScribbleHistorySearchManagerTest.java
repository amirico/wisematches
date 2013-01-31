package wisematches.playground.scribble.history;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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
public class ScribbleHistorySearchManagerTest {
	@Test
	public void commented() {
		throw new UnsupportedOperationException("Commented");
	}
/*
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
		if (finishedGamesCount < 4) {
			assertEquals(0, historyEntity2.size());
		} else if (finishedGamesCount > 10) {
			assertEquals(10, historyEntity2.size());
		} else {
			assertEquals(finishedGamesCount, historyEntity2.size());
		}

		historySearchManager.searchEntities(player, GameResolution.FINISHED, null, null, null);
		historySearchManager.searchEntities(player, GameResolution.RESIGNED, null, null, null);
		historySearchManager.searchEntities(player, GameResolution.STALEMATE, null, null, null);
		historySearchManager.searchEntities(player, GameResolution.INTERRUPTED, null, null, null);
	}
*/
}
