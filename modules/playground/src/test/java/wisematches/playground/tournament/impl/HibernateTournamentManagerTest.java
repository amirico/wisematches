package wisematches.playground.tournament.impl;

import org.junit.Ignore;
import org.junit.Test;
import wisematches.personality.Language;
import wisematches.playground.tournament.TournamentGroupId;
import wisematches.playground.tournament.TournamentRoundId;
import wisematches.playground.tournament.TournamentSection;
import wisematches.playground.tournament.TournamentSectionId;

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
