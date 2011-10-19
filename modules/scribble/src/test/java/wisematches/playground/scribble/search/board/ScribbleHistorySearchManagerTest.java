package wisematches.playground.scribble.search.board;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.database.Order;
import wisematches.database.Range;
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
public class ScribbleHistorySearchManagerTest {
	@Autowired
	private ScribbleHistorySearchManager historySearchManager;

	public ScribbleHistorySearchManagerTest() {
	}

	@Test
	public void testName() throws Exception {
		final Personality player = Personality.person(1029);

		int finishedGamesCount = historySearchManager.getTotalCount(player, null);
		System.out.println(finishedGamesCount);

		List<ScribbleHistoryEntity> historyEntity = historySearchManager.searchEntities(player, null, null, new Order[]{Order.asc("language")}, Range.limit(4, 10));
		System.out.println("Finished size: " + historyEntity.size());
		System.out.println(historyEntity);
	}
}
