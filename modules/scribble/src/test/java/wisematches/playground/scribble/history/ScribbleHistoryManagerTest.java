package wisematches.playground.scribble.history;

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
public class ScribbleHistoryManagerTest {
	@Autowired
	private ScribbleHistoryManager historyManager;

	public ScribbleHistoryManagerTest() {
	}

	@Test
	public void testFinishedGames() {
		final Personality player = Personality.person(1029);

		int finishedGamesCount = historyManager.getFinishedGamesCount(player);
		System.out.println(finishedGamesCount);

		List<ScribbleGameHistory> finishedGames = historyManager.getFinishedGames(player, Range.limit(1, 10), Order.asc("language"));
		System.out.println("Finished size: " + finishedGames.size());
		System.out.println(finishedGames);
	}
}
