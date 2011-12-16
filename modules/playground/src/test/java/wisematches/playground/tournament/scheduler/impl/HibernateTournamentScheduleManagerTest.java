package wisematches.playground.tournament.scheduler.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Language;
import wisematches.personality.player.Player;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.playground.tournament.TournamentSection;
import wisematches.playground.tournament.scheduler.TournamentPoster;
import wisematches.playground.tournament.scheduler.TournamentTicket;

import java.util.Iterator;

import static org.junit.Assert.*;

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
public class HibernateTournamentScheduleManagerTest {
	@Autowired
	private HibernateTournamentScheduleManager tournamentScheduleManager;

	public HibernateTournamentScheduleManagerTest() {
	}

	@Test
	public void test() {
		final Player p1 = RobotPlayer.DULL;

		final TournamentPoster poster = tournamentScheduleManager.getTournamentPoster();
		assertNotNull("No poster?", poster);

		assertNull(tournamentScheduleManager.getPlayerTicket(p1, Language.EN));
		assertNull(tournamentScheduleManager.getPlayerTicket(p1, Language.RU));

		tournamentScheduleManager.buyTicket(p1, Language.EN, TournamentSection.ADVANCED);
		assertEquals(TournamentSection.ADVANCED, tournamentScheduleManager.getPlayerTicket(p1, Language.EN).getSection());
		assertNull(tournamentScheduleManager.getPlayerTicket(p1, Language.RU));
		assertEquals(1, tournamentScheduleManager.getPlayerTickets(p1).size());

		tournamentScheduleManager.buyTicket(p1, Language.RU, TournamentSection.EXPERT);
		assertEquals(TournamentSection.ADVANCED, tournamentScheduleManager.getPlayerTicket(p1, Language.EN).getSection());
		assertEquals(TournamentSection.EXPERT, tournamentScheduleManager.getPlayerTicket(p1, Language.RU).getSection());
		assertEquals(2, tournamentScheduleManager.getPlayerTickets(p1).size());

		final Iterator<TournamentTicket> iterator = tournamentScheduleManager.getPlayerTickets(p1).iterator();
		final TournamentTicket n1 = iterator.next();
		assertEquals(Language.EN, n1.getLanguage());
		assertEquals(TournamentSection.ADVANCED, n1.getSection());

		final TournamentTicket n2 = iterator.next();
		assertEquals(Language.RU, n2.getLanguage());
		assertEquals(TournamentSection.EXPERT, n2.getSection());

		tournamentScheduleManager.sellTicket(p1, Language.EN);
		assertNull(tournamentScheduleManager.getPlayerTicket(p1, Language.EN));
		assertEquals(TournamentSection.EXPERT, tournamentScheduleManager.getPlayerTicket(p1, Language.RU).getSection());
		assertEquals(1, tournamentScheduleManager.getPlayerTickets(p1).size());

		tournamentScheduleManager.sellTicket(p1, Language.RU);
		assertNull(tournamentScheduleManager.getPlayerTicket(p1, Language.RU));
		assertNull(tournamentScheduleManager.getPlayerTicket(p1, Language.RU));
		assertEquals(0, tournamentScheduleManager.getPlayerTickets(p1).size());
	}
}
