package wisematches.core.personality.player.membership.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.personality.player.membership.MembershipManager;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/properties-config.xml",
		"classpath:/config/database-config.xml",
		"classpath:/config/personality-config.xml"
})
public class HibernateMembershipManagerTest {
	@Autowired
	private MembershipManager membershipManager;

	public HibernateMembershipManagerTest() {
	}

	@Test
	public void testCleanup() {
		membershipManager.cleanup(new Date());
	}
}
