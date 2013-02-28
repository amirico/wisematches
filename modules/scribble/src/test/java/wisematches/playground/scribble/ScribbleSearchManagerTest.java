package wisematches.playground.scribble;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Personality;
import wisematches.core.RobotType;
import wisematches.core.personality.DefaultRobot;
import wisematches.core.search.Order;
import wisematches.core.search.Orders;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/properties-config.xml",
		"classpath:/config/database-config.xml",
		"classpath:/config/personality-config.xml",
		"classpath:/config/scribble-config.xml"
})
public class ScribbleSearchManagerTest {
	@Autowired
	private ScribbleSearchManager searchManager;

	public ScribbleSearchManagerTest() {
	}

	@Test
	public void testFinishedGames() throws Exception {
		final Personality p = new DefaultRobot(RobotType.DULL);

		final ScribbleContext context = new ScribbleContext(false);
		final int totalCount = searchManager.getTotalCount(p, context);
		System.out.println("Finished games: " + totalCount);

		final List<ScribbleDescription> boards = searchManager.searchEntities(p, context, Orders.all(Order.desc("finishedDate")), null);
		assertEquals(totalCount, boards.size());

		final ScribbleDescription board = boards.get(0);

		final List<Personality> players = board.getPlayers();
		assertNotNull(players.get(0));

	}

	@Test
	public void testActiveGames() throws Exception {
		final Personality p = new DefaultRobot(RobotType.DULL);

		final ScribbleContext context = new ScribbleContext(true);
		final int totalCount = searchManager.getTotalCount(p, context);
		System.out.println("Active games: " + totalCount);

		final List<ScribbleDescription> boards = searchManager.searchEntities(p, context, Orders.all(Order.desc("finishedDate")), null);
		assertEquals(totalCount, boards.size());
	}
}
