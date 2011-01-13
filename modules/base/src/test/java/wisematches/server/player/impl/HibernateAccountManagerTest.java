package wisematches.server.player.impl;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.server.player.*;

import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/test-server-base-config.xml",
		"classpath:/config/database-config.xml",
		"classpath:/config/server-base-config.xml"
})
public class HibernateAccountManagerTest {
	@Autowired
	private AccountManager accountManager;

	@Autowired
	private SessionFactory sessionFactory;

	@Test
	public void testCreateAccount() throws Exception {
		final String id = UUID.randomUUID().toString();
		final Player mock = accountManager.createPlayer(new PlayerDetails(id + "@wisematches.net", id, "mock", Language.ENGLISH, Membership.GUEST));
		sessionFactory.getCurrentSession().flush();

		assertNotNull(mock);
		assertFalse(0 == mock.getId());
		System.out.println("Assigned player id: " + mock.getId());
	}
}
