package wisematches.playground.award.impl;

import junit.framework.Assert;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.playground.award.AwardsSummary;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml"
})
public class HibernateAwardsManagerTest {
	@Autowired
	private SessionFactory sessionFactory;

	private HibernateAwardsManager awardsManager;

	public HibernateAwardsManagerTest() {
	}

	@Before
	public void setUp() {
		awardsManager = new HibernateAwardsManager();
		awardsManager.setSessionFactory(sessionFactory);
	}

	@Test
	public void testGetAwardsSummary() {
		final AwardsSummary awardsSummary = awardsManager.getAwardsSummary(Personality.person(1001L));
		Assert.assertNotNull(awardsSummary);
	}

	@Test
	public void testSearch() {
//		awardsManager.getTotalCount(Personality.person(1001L), null);
//		awardsManager.getTotalCount(Personality.person(1001L), null);
	}
}
