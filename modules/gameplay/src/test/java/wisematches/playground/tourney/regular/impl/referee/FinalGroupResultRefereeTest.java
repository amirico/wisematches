package wisematches.playground.tourney.regular.impl.referee;

import org.junit.Test;
import wisematches.playground.tourney.TourneyPlace;
import wisematches.playground.tourney.TourneyWinner;
import wisematches.playground.tourney.regular.TourneyGroup;
import wisematches.playground.tourney.regular.impl.HibernateTourneyWinner;

import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

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

		final List<HibernateTourneyWinner> winnersList = resultReferees.getWinnersList(group, null, null);
		assertEquals(4, winnersList.size());

		assertWinner(101L, TourneyPlace.FIRST, winnersList.get(0));
		assertWinner(102L, TourneyPlace.FIRST, winnersList.get(1));
		assertWinner(103L, TourneyPlace.SECOND, winnersList.get(2));
		assertWinner(104L, TourneyPlace.THIRD, winnersList.get(3));

		verify(group);
	}

	private void assertWinner(final long player, final TourneyPlace place, final TourneyWinner winner) {
		assertEquals(player, winner.getPlayer().longValue());
		assertEquals(place, winner.getPlace());
	}
}
