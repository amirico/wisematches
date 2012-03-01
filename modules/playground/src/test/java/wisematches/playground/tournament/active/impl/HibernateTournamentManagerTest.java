package wisematches.playground.tournament.active.impl;

import org.junit.Ignore;
import org.junit.Test;
import wisematches.playground.search.SearchManager;
import wisematches.playground.tournament.*;

import static org.junit.Assert.assertNotNull;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Ignore
public class HibernateTournamentManagerTest {
	public HibernateTournamentManagerTest() {
	}

	@Test
	public void testSearchManager() {
		final HibernateTournamentManager m = new HibernateTournamentManager();

		SearchManager<TournamentSection, TournamentId> sm = m.getTournamentSearchManager(TournamentSection.class);
		assertNotNull(sm);

		SearchManager<TournamentRound, TournamentSectionId> rm = m.getTournamentSearchManager(TournamentRound.class);
		assertNotNull(rm);

		SearchManager<TournamentGroup, TournamentRoundId> gm = m.getTournamentSearchManager(TournamentGroup.class);
		assertNotNull(gm);
	}
}
