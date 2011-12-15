package wisematches.playground.tournament.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Language;
import wisematches.personality.player.Player;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.playground.tournament.Tournament;
import wisematches.playground.tournament.TournamentSection;
import wisematches.playground.tournament.TournamentSubscriptionException;
import wisematches.playground.tournament.TournamentSubscriptionListener;

import static org.easymock.EasyMock.createStrictMock;
import static org.junit.Assert.assertNotNull;

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
public class HibernateTournamentManagerTest {
	@Autowired
	private HibernateTournamentManager tournamentManager;

	public HibernateTournamentManagerTest() {
	}

	@Test
	public void testAnnouncedTournament() {
		final Tournament tournament = tournamentManager.getAnnouncedTournament();
		assertNotNull(tournament);
	}

	@Test
	public void testSubscription() throws TournamentSubscriptionException {
		final Player p1 = RobotPlayer.DULL;
		final Player p2 = RobotPlayer.EXPERT;
		final Tournament tournament = tournamentManager.getAnnouncedTournament();

		final TournamentSubscriptionListener l = createStrictMock(TournamentSubscriptionListener.class);
		l.tournamentSubscribed(tournament, p1, null);

		tournamentManager.addTournamentSubscriptionListener(l);

		tournamentManager.subscribe(p1, Language.EN, TournamentSection.ADVANCED);
		tournamentManager.subscribe(p1, Language.RU, TournamentSection.ADVANCED);
		tournamentManager.subscribe(p1, Language.EN, TournamentSection.CASUAL);

		tournamentManager.removeTournamentSubscriptionListener(l);
	}
}
