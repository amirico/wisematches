package wisematches.server.services.relations.blacklist.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Personality;
import wisematches.core.Player;
import wisematches.core.personality.DefaultMember;
import wisematches.server.services.relations.blacklist.BlacklistManager;
import wisematches.server.services.relations.blacklist.BlacklistRecord;
import wisematches.server.services.relations.blacklist.BlacklistedException;

import java.util.Collection;

import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/properties-config.xml",
		"classpath:/config/database-config.xml",
		"classpath:/config/personality-config.xml",
		"classpath:/config/playground-config.xml",
		"classpath:/config/scribble-config.xml",
})
public class HibernateBlacklistManagerTest {
	@Autowired
	private BlacklistManager blacklistManager;

	public HibernateBlacklistManagerTest() {
	}

	@Test
	public void test() throws BlacklistedException {
		final Player p1 = new DefaultMember(901, null, null, null, null, null);
		final Player p2 = new DefaultMember(902, null, null, null, null, null);

		blacklistManager.checkBlacklist(p1, p2);
		assertFalse(blacklistManager.isBlacklisted(p1, p2));
		assertEquals(0, blacklistManager.getBlacklist(p1).size());

		blacklistManager.addPlayer(p1, p2, "Comment1");
		final Collection<BlacklistRecord> records1 = blacklistManager.getBlacklist(p1);
		assertEquals(1, records1.size());
		assertTrue(blacklistManager.isBlacklisted(p1, p2));
		try {
			blacklistManager.checkBlacklist(p1, p2);
			fail("Exception must be here");
		} catch (BlacklistedException ignore) {
		}
		assertRecord((BlacklistRecord) records1.toArray()[0], p1, p2, "Comment1");

		blacklistManager.addPlayer(p1, p2, "Comment2");
		final Collection<BlacklistRecord> records2 = blacklistManager.getBlacklist(p1);
		assertEquals(1, records2.size());
		assertTrue(blacklistManager.isBlacklisted(p1, p2));
		try {
			blacklistManager.checkBlacklist(p1, p2);
			fail("Exception must be here");
		} catch (BlacklistedException ignore) {
		}
		assertRecord((BlacklistRecord) records2.toArray()[0], p1, p2, "Comment2");

		blacklistManager.removePlayer(p1, p2);
		final Collection<BlacklistRecord> records3 = blacklistManager.getBlacklist(p1);
		assertFalse(blacklistManager.isBlacklisted(p1, p2));
		blacklistManager.checkBlacklist(p1, p2);
		assertEquals(0, records3.size());
	}

	private void assertRecord(BlacklistRecord record, Personality player, Personality whom, String c) {
		assertEquals(player.getId().longValue(), record.getPerson());
		assertEquals(whom.getId().longValue(), record.getWhom());
		assertEquals(c, record.getMessage());
	}
}
