package wisematches.playground.tourney.regular.impl;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.playground.tourney.TourneyPlace;
import wisematches.playground.tourney.regular.TourneyAward;

import java.util.EnumSet;

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
public class HibernateAwardsSearchManagerTest {
	@Autowired
	private SessionFactory sessionFactory;

	public HibernateAwardsSearchManagerTest() {
	}

	@Test
	public void testSearchManager() {
		HibernateAwardsSearchManager manager = new HibernateAwardsSearchManager();
		manager.setSessionFactory(sessionFactory);

		System.out.println(manager.getTotalCount(Personality.person(1027L), null));
		System.out.println(manager.getTotalCount(Personality.person(1027L), new TourneyAward.Context(EnumSet.of(TourneyPlace.FIRST))));

		System.out.println(manager.searchEntities(Personality.person(1027L), null, null, null, null));
		System.out.println(manager.searchEntities(Personality.person(1027L), new TourneyAward.Context(EnumSet.of(TourneyPlace.FIRST)), null, null, null));
	}
}
