package wisematches.playground.scribble.history;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.database.Range;
import wisematches.personality.Personality;
import wisematches.playground.history.GameHistory;

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
	public void test() {
		final Personality player = Personality.person(1029);

		int finishedGamesCount = historyManager.getFinishedGamesCount(player);
		System.out.println(finishedGamesCount);

		List<GameHistory> finishedGames = historyManager.getFinishedGames(player, Range.limit(14, 3));
		System.out.println("Finished size: " + finishedGames.size());
		System.out.println(finishedGames);
	}
}
