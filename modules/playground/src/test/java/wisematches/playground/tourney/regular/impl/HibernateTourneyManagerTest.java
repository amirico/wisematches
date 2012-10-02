package wisematches.playground.tourney.regular.impl;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Language;
import wisematches.playground.tourney.regular.*;

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
public class HibernateTourneyManagerTest {
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private RegularTourneyManager regularTourneyManager;

	public HibernateTourneyManagerTest() {
	}

	@Test
	public void test_getTournamentEntity() {
		final RegularTourney tourney = regularTourneyManager.getTournamentEntity(new RegularTourney.Id(1));
		final TourneyDivision division = regularTourneyManager.getTournamentEntity(new TourneyDivision.Id(1, Language.EN, TourneySection.ADVANCED));
		final TourneyRound round = regularTourneyManager.getTournamentEntity(new TourneyRound.Id(1, Language.EN, TourneySection.ADVANCED, 1));
		final TourneyGroup group = regularTourneyManager.getTournamentEntity(new TourneyGroup.Id(1, Language.EN, TourneySection.ADVANCED, 1, 1));

		System.out.println("asd");
	}
}
