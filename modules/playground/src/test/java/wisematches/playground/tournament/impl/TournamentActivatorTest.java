package wisematches.playground.tournament.impl;

import org.junit.Test;

import java.text.ParseException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TournamentActivatorTest {
	public TournamentActivatorTest() {
	}

	@Test
	public void test() throws ParseException {
		TournamentActivator a = new TournamentActivator();
//		a.setCronExpression("0 30 2 ? * 2#1");
		a.setCronExpression("* * * ? * 6#4");
		System.out.println(a.getNextTournamentDay().toGMTString());
		System.out.println(a.isTournamentDay());
	}
}
