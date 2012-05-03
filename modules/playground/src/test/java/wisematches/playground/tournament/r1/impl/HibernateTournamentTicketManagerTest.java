package wisematches.playground.tournament.r1.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional()
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml",
		"classpath:/config/accounts-config.xml",
		"classpath:/config/playground-config.xml"
})
public class HibernateTournamentTicketManagerTest {
//	@Autowired
//	private TournamentTicketManager tournamentTicketManager;

	public HibernateTournamentTicketManagerTest() {
	}

/*
	@Test
	public void testBuySell() {
		final Player p1 = RobotPlayer.DULL;

		tournamentTicketManager.announceTournament(new Date(System.currentTimeMillis() + 100000));
		final TournamentPoster poster = tournamentTicketManager.getTournamentPoster();
		assertNotNull("No poster?", poster);

		assertNull(tournamentTicketManager.getPlayerTicket(p1, Language.EN));
		assertNull(tournamentTicketManager.getPlayerTicket(p1, Language.RU));

		tournamentTicketManager.buyTicket(p1, Language.EN, TournamentSection.ADVANCED);
		assertEquals(TournamentSection.ADVANCED, tournamentTicketManager.getPlayerTicket(p1, Language.EN).getSection());
		assertNull(tournamentTicketManager.getPlayerTicket(p1, Language.RU));
		assertEquals(1, tournamentTicketManager.getPlayerTickets(p1).size());

		tournamentTicketManager.buyTicket(p1, Language.RU, TournamentSection.EXPERT);
		assertEquals(TournamentSection.ADVANCED, tournamentTicketManager.getPlayerTicket(p1, Language.EN).getSection());
		assertEquals(TournamentSection.EXPERT, tournamentTicketManager.getPlayerTicket(p1, Language.RU).getSection());
		assertEquals(2, tournamentTicketManager.getPlayerTickets(p1).size());

		final Iterator<TournamentTicket> iterator = tournamentTicketManager.getPlayerTickets(p1).iterator();
		final TournamentTicket n1 = iterator.next();
		assertEquals(Language.EN, n1.getLanguage());
		assertEquals(TournamentSection.ADVANCED, n1.getSection());

		final TournamentTicket n2 = iterator.next();
		assertEquals(Language.RU, n2.getLanguage());
		assertEquals(TournamentSection.EXPERT, n2.getSection());

		tournamentTicketManager.sellTicket(p1, Language.EN);
		assertNull(tournamentTicketManager.getPlayerTicket(p1, Language.EN));
		assertEquals(TournamentSection.EXPERT, tournamentTicketManager.getPlayerTicket(p1, Language.RU).getSection());
		assertEquals(1, tournamentTicketManager.getPlayerTickets(p1).size());

		tournamentTicketManager.sellTicket(p1, Language.RU);
		assertNull(tournamentTicketManager.getPlayerTicket(p1, Language.RU));
		assertNull(tournamentTicketManager.getPlayerTicket(p1, Language.RU));
		assertEquals(0, tournamentTicketManager.getPlayerTickets(p1).size());
	}
*/

	@Test
	public void testTournamentTicketBatch() {
/*
		final Player p1 = RobotPlayer.DULL;
		final Player p2 = RobotPlayer.TRAINEE;
		final Player p3 = RobotPlayer.EXPERT;

		tournamentTicketManager.buyTicket(p1, Language.EN, TournamentSection.ADVANCED);
		tournamentTicketManager.buyTicket(p2, Language.EN, TournamentSection.CASUAL);
		tournamentTicketManager.buyTicket(p3, Language.EN, TournamentSection.ADVANCED);

		tournamentTicketManager.buyTicket(p1, Language.RU, TournamentSection.EXPERT);
		tournamentTicketManager.buyTicket(p2, Language.RU, TournamentSection.EXPERT);
		tournamentTicketManager.buyTicket(p3, Language.RU, TournamentSection.EXPERT);

		final TournamentTicketBatch i1 = tournamentTicketManager.getTournamentTicketBatch(Language.EN);
		assertEquals(3, i1.getTotalTickets());
		assertEquals(2, i1.getBoughtTicketsCount(TournamentSection.ADVANCED));
		assertEquals(1, i1.getBoughtTicketsCount(TournamentSection.CASUAL));
		assertEquals(0, i1.getBoughtTicketsCount(TournamentSection.EXPERT));
		assertEquals(0, i1.getBoughtTicketsCount(TournamentSection.GRANDMASTER));
		assertEquals(0, i1.getBoughtTicketsCount(TournamentSection.INTERMEDIATE));

		final TournamentTicketBatch i2 = tournamentTicketManager.getTournamentTicketBatch(Language.RU);
		assertEquals(3, i2.getTotalTickets());
		assertEquals(0, i2.getBoughtTicketsCount(TournamentSection.ADVANCED));
		assertEquals(0, i2.getBoughtTicketsCount(TournamentSection.CASUAL));
		assertEquals(3, i2.getBoughtTicketsCount(TournamentSection.EXPERT));
		assertEquals(0, i2.getBoughtTicketsCount(TournamentSection.GRANDMASTER));
		assertEquals(0, i2.getBoughtTicketsCount(TournamentSection.INTERMEDIATE));
*/
	}
}
