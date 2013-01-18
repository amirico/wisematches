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
import wisematches.core.Personality;
import wisematches.core.search.Order;
import wisematches.core.search.Orders;
import wisematches.core.search.Range;
import wisematches.playground.award.AwardContext;
import wisematches.playground.award.AwardWeight;
import wisematches.playground.award.AwardsSummary;

import java.util.EnumSet;

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
		final Personality person = Personality.person(1001L);

		final AwardContext ctx1 = new AwardContext("moc", null);
		awardsManager.getTotalCount(person, ctx1);
		awardsManager.searchEntities(Personality.person(1001L), ctx1, null, Orders.of(Order.desc("awardedDate")), Range.limit(10));

		final AwardContext ctx2 = new AwardContext("moc", EnumSet.of(AwardWeight.SILVER));
		awardsManager.getTotalCount(person, ctx2);
		awardsManager.searchEntities(Personality.person(1001L), ctx2, null, Orders.of(Order.desc("awardedDate")), Range.limit(10));
	}
}
