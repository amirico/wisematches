package wisematches.playground.tourney.regular.impl.referee;

import org.junit.Test;
import wisematches.playground.tourney.regular.TourneyGroup;
import wisematches.playground.tourney.regular.TourneyWinner;
import wisematches.playground.tourney.regular.WinnerPlace;
import wisematches.playground.tourney.regular.impl.HibernateTourneyWinner;

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

		final TourneyGroup group = createMock(TourneyGroup.class);
		expect(group.getScores()).andReturn(new short[]{2, 1, 2, 0});
		expect(group.getPlayers()).andReturn(new long[]{101L, 103L, 102L, 104L}).times(WinnerPlace.values().length);
		expect(group.getScores(101L)).andReturn((short) 2).times(WinnerPlace.values().length);
		expect(group.getScores(102L)).andReturn((short) 2).times(WinnerPlace.values().length);
		expect(group.getScores(103L)).andReturn((short) 1).times(WinnerPlace.values().length);
		expect(group.getScores(104L)).andReturn((short) 0).times(WinnerPlace.values().length);
		replay(group);

		final List<HibernateTourneyWinner> winnersList = resultReferees.getWinnersList(group, null, null);
		assertEquals(4, winnersList.size());

		assertWinner(101L, WinnerPlace.FIRST, winnersList.get(0));
		assertWinner(102L, WinnerPlace.FIRST, winnersList.get(1));
		assertWinner(103L, WinnerPlace.SECOND, winnersList.get(2));
		assertWinner(104L, WinnerPlace.THIRD, winnersList.get(3));

		verify(group);
	}

	private void assertWinner(final long player, final WinnerPlace place, final TourneyWinner winner) {
		assertEquals(player, winner.getPlayer());
		assertEquals(place, winner.getPlace());
	}
}
