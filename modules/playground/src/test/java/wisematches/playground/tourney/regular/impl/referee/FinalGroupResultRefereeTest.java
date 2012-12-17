package wisematches.playground.tourney.regular.impl.referee;

import org.junit.Test;
import wisematches.playground.tourney.TourneyConqueror;
import wisematches.playground.tourney.TourneyMedal;
import wisematches.playground.tourney.regular.TourneyGroup;
import wisematches.playground.tourney.regular.impl.HibernateTourneyConqueror;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FinalGroupResultRefereeTest {
	public FinalGroupResultRefereeTest() {
	}

	@Test
	public void testGetWinnersList() throws Exception {
		final FinalGroupResultReferee resultReferees = new FinalGroupResultReferee();

		final TourneyGroup group = createStrictMock(TourneyGroup.class);
		expect(group.getPlayers()).andReturn(new long[]{101L, 103L, 102L, 104L});
		expect(group.getPlayerScores(101L)).andReturn(2);
		expect(group.getPlayerScores(103L)).andReturn(1);
		expect(group.getPlayerScores(102L)).andReturn(2);
		expect(group.getPlayerScores(104L)).andReturn(0);
		replay(group);

		final List<HibernateTourneyConqueror> winnersList = resultReferees.getWinnersList(group, null, null);
		assertEquals(4, winnersList.size());

		assertWinner(101L, TourneyMedal.GOLD, winnersList.get(0));
		assertWinner(102L, TourneyMedal.GOLD, winnersList.get(1));
		assertWinner(103L, TourneyMedal.SILVER, winnersList.get(2));
		assertWinner(104L, TourneyMedal.BRONZE, winnersList.get(3));

		verify(group);
	}

	private void assertWinner(final long player, final TourneyMedal place, final TourneyConqueror winner) {
		assertEquals(player, winner.getPlayer());
		assertEquals(place, winner.getPlace());
	}
}
