package wisematches.playground.tournament;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TournamentSectionTest {
	public TournamentSectionTest() {
	}

	@Test
	public void testGetHigherSection() {
		Assert.assertEquals(TournamentSection.INTERMEDIATE, TournamentSection.CASUAL.getHigherSection());
		Assert.assertEquals(TournamentSection.ADVANCED, TournamentSection.INTERMEDIATE.getHigherSection());
		Assert.assertEquals(TournamentSection.EXPERT, TournamentSection.ADVANCED.getHigherSection());
		Assert.assertEquals(TournamentSection.GRANDMASTER, TournamentSection.EXPERT.getHigherSection());
		Assert.assertNull(TournamentSection.GRANDMASTER.getHigherSection());
	}
}
