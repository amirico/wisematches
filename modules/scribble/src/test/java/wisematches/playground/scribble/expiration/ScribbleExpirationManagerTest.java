package wisematches.playground.scribble.expiration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
        "classpath:/config/accounts-config.xml",
        "classpath:/config/playground-config.xml",
        "classpath:/config/scribble-junit-config.xml"
})
public class ScribbleExpirationManagerTest {
    @Autowired
    private ScribbleExpirationManager expirationManager;

    public ScribbleExpirationManagerTest() {
    }

    @Test
    public void testName() throws Exception {
        throw new UnsupportedOperationException("TODO: commented");
/*
		final Personality player = Personality.person(1029);

		final int finishedGamesCount = expirationManager.getTotalCount(player, null);
		final List<ExpirationDescriptor> descriptors = expirationManager.searchEntities(player, null, null, null, null);
		assertEquals(finishedGamesCount, descriptors.size());

		expirationManager.searchEntities(player, null, null, Orders.of(Order.desc("daysPerMove"), Order.asc("lastMoveTime"), Order.asc("boardId")), null);
		expirationManager.searchEntities(player, null, null, null, Range.limit(1, 3));
*/
    }
}
