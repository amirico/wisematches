package wisematches.personality.membership.impl;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Membership;
import wisematches.personality.Personality;
import wisematches.personality.account.AccountManager;
import wisematches.personality.membership.MembershipActivation;

import static org.easymock.EasyMock.createStrictMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * TODO: ignored
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Ignore
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml"
})
public class HibernateMembershipManagerTest {
	@Autowired
	private SessionFactory sessionFactory;

	private AccountManager accountManager;
	private HibernateMembershipManager membershipManager;

	public HibernateMembershipManagerTest() {
	}

	@Before
	public void setUp() {
		accountManager = createStrictMock(AccountManager.class);

		membershipManager = new HibernateMembershipManager();
		membershipManager.setSessionFactory(sessionFactory);
		membershipManager.setAccountManager(accountManager);
	}

	@Test
	public void testActivateMembership() {
		final Personality person = Personality.person(101L);

//		expect(accountManager.getAccount(person.getId())).;
//		replay(accountManager);

		final MembershipActivation record = membershipManager.activateMembership(person, Membership.SILVER, 1, null);
		assertEquals(101L, record.getPlayer());
		assertEquals(1, record.getTotalDays());
		assertEquals(0, record.getSpentDays());
		assertEquals(Membership.SILVER, record.getMembership());
		assertNotNull(record.getRegistered());

		final HibernateActiveMembership m = (HibernateActiveMembership) sessionFactory.getCurrentSession().get(HibernateActiveMembership.class, person.getId());
		assertNotNull(m.getActivation());

//		verify(accountManager);
	}
}
