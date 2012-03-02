package wisematches.playground.tournament.active.impl;

import org.junit.Ignore;
import org.junit.Test;
import wisematches.personality.Language;
import wisematches.playground.search.SearchManager;
import wisematches.playground.tournament.*;

import java.util.List;

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

		TournamentSectionId context = TournamentSectionId.valueOf(0, Language.RU, TournamentSection.GRANDMASTER);
		TournamentRoundId context1 = TournamentRoundId.valueOf(0, Language.RU, TournamentSection.GRANDMASTER, 12);
		TournamentGroupId context2 = TournamentGroupId.valueOf(0, Language.RU, TournamentSection.GRANDMASTER, 12, 13);

		int totalCount1 = m.getTotalCount(null, context);
		int totalCount2 = m.getTotalCount(null, context1);
		int totalCount3 = m.getTotalCount(null, context2);

//		List<TournamentRound> list = m.searchEntities(null, context, null, null, null);
	}
}
