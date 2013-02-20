package wisematches.server.services.award.impl;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Player;
import wisematches.core.personality.DefaultMember;
import wisematches.core.search.Order;
import wisematches.core.search.Orders;
import wisematches.core.search.Range;
import wisematches.server.services.award.AwardContext;
import wisematches.server.services.award.AwardWeight;
import wisematches.server.services.award.AwardsSummary;

import java.util.EnumSet;

import static org.junit.Assert.assertNotNull;

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

	final Player person = new DefaultMember(901, null, null, null, null, null);

	public HibernateAwardsManagerTest() {
	}

	@Before
	public void setUp() {
		awardsManager = new HibernateAwardsManager();
		awardsManager.setSessionFactory(sessionFactory);
	}

	@Test
	public void testGetAwardsSummary() {
		final AwardsSummary awardsSummary = awardsManager.getAwardsSummary(person);
		assertNotNull(awardsSummary);
	}

	@Test
	public void testSearch() {

		final AwardContext ctx1 = new AwardContext("moc", null);
		awardsManager.getTotalCount(person, ctx1);
		awardsManager.searchEntities(person, ctx1, Orders.of(Order.desc("awardedDate")), Range.limit(10));

		final AwardContext ctx2 = new AwardContext("moc", EnumSet.of(AwardWeight.SILVER));
		awardsManager.getTotalCount(person, ctx2);
		awardsManager.searchEntities(person, ctx2, Orders.of(Order.desc("awardedDate")), Range.limit(10));
	}
}
