package wisematches.playground.blacklist.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.playground.blacklist.BlacklistManager;
import wisematches.playground.blacklist.BlacklistRecord;

import java.util.Collection;

import static junit.framework.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/config/database-junit-config.xml",
        "classpath:/config/accounts-config.xml",
        "classpath:/config/playground-config.xml"
})
public class HibernateBlacklistManagerTest {
    @Autowired
    private BlacklistManager blacklistManager;

    public HibernateBlacklistManagerTest() {
    }

    @Test
    public void test() {
        final Personality p1 = Personality.person(1);
        final Personality p2 = Personality.person(2);

        assertFalse(blacklistManager.isIgnored(p1, p2));
        assertEquals(0, blacklistManager.getBlacklist(p1).size());

        blacklistManager.addPlayer(p1, p2, "Comment1");
        final Collection<BlacklistRecord> records1 = blacklistManager.getBlacklist(p1);
        assertEquals(1, records1.size());
        assertTrue(blacklistManager.isIgnored(p1, p2));
        assertRecord((BlacklistRecord) records1.toArray()[0], p1, p2, "Comment1");

        blacklistManager.addPlayer(p1, p2, "Comment2");
        final Collection<BlacklistRecord> records2 = blacklistManager.getBlacklist(p1);
        assertEquals(1, records2.size());
        assertTrue(blacklistManager.isIgnored(p1, p2));
        assertRecord((BlacklistRecord) records2.toArray()[0], p1, p2, "Comment2");

        blacklistManager.removePlayer(p1, p2);
        final Collection<BlacklistRecord> records3 = blacklistManager.getBlacklist(p1);
        assertFalse(blacklistManager.isIgnored(p1, p2));
        assertEquals(0, records3.size());
    }

    private void assertRecord(BlacklistRecord record, Personality player, Personality whom, String c) {
        assertEquals(player.getId(), record.getPerson());
        assertEquals(whom.getId(), record.getWhom());
        assertEquals(c, record.getMessage());
    }
}
