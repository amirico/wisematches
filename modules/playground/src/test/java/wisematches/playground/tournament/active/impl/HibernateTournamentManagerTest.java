package wisematches.playground.tournament.active.impl;

import org.junit.Assert;
import org.junit.Test;
import wisematches.playground.search.EntitySearchManager;
import wisematches.playground.tournament.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTournamentManagerTest {
	public HibernateTournamentManagerTest() {
	}

	@Test
	public void testSearchManager() {
		final HibernateTournamentManager m = new HibernateTournamentManager();

		EntitySearchManager<TournamentSection, TournamentId> sm = m.getTournamentSearchManager(TournamentSection.class);
		assertNotNull(sm);

		EntitySearchManager<TournamentRound, TournamentSectionId> rm = m.getTournamentSearchManager(TournamentRound.class);
		assertNotNull(rm);

		EntitySearchManager<TournamentGroup, TournamentRoundId> gm = m.getTournamentSearchManager(TournamentGroup.class);
		assertNotNull(gm);
	}
}
