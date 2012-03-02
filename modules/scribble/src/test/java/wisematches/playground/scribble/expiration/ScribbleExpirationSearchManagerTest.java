package wisematches.playground.scribble.expiration;

import org.junit.Assert;
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
import wisematches.playground.expiration.GameExpirationDescriptor;
import wisematches.playground.scribble.history.ScribbleHistoryEntity;
import wisematches.playground.scribble.history.ScribbleHistorySearchManager;

import java.util.List;

import static org.junit.Assert.*;

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
public class ScribbleExpirationSearchManagerTest {
	@Autowired
	private ScribbleExpirationSearchManager expirationSearchManager;

	public ScribbleExpirationSearchManagerTest() {
	}

	@Test
	public void testName() throws Exception {
		final Personality player = Personality.person(1029);

		final int finishedGamesCount = expirationSearchManager.getTotalCount(player, null);
		final List<GameExpirationDescriptor> descriptors = expirationSearchManager.searchEntities(player, null, null, null, null);
		assertEquals(finishedGamesCount, descriptors.size());

		expirationSearchManager.searchEntities(player, null, null, Orders.of(Order.desc("daysPerMove"), Order.asc("lastMoveTime"), Order.asc("boardId")), null);
		expirationSearchManager.searchEntities(player, null, null, null, Range.limit(1, 3));
	}
}
