package wisematches.playground.tourney.regular.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
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
		"classpath:/config/playground-config.xml"
})
public class HibernateRegularTourneyManagerTest {
//	@Autowired
//	RegularTourneyManager regularTourneyManager;

	public HibernateRegularTourneyManagerTest() {
	}

	@Test
	public void mock() {
	}
}
