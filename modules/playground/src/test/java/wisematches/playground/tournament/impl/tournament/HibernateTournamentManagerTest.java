package wisematches.playground.tournament.impl.tournament;

import org.junit.Ignore;
import org.junit.Test;
import wisematches.personality.Language;
import wisematches.playground.tournament.*;

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

        TournamentSectionId context = TournamentSectionId.valueOf(0, Language.RU, TournamentCategory.GRANDMASTER);
        TournamentRoundId context1 = TournamentRoundId.valueOf(0, Language.RU, TournamentCategory.GRANDMASTER, 12);
        TournamentGroupId context2 = TournamentGroupId.valueOf(0, Language.RU, TournamentCategory.GRANDMASTER, 12, 13);

        int totalCount1 = m.getTotalCount(null, context);
        int totalCount2 = m.getTotalCount(null, context1);
        int totalCount3 = m.getTotalCount(null, context2);

//        final List<TournamentEntity<TournamentEntityId>> tournamentEntities = m.searchEntities(null, TournamentSectionId.valueOf(1, Language.EN, TournamentCategory.ADVANCED), null, null, null);

//		List<TournamentRound> list = m.searchEntities(null, context, null, null, null);
    }
}
