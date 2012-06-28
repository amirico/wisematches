package wisematches.playground.tournament.impl.fsm;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class InitializingRound implements TSMActivityContext {
	private final int tournament;
	private final int round;

	public InitializingRound(int tournament, int round) {
		this.tournament = tournament;
		this.round = round;
	}

	public int getTournament() {
		return tournament;
	}

	public int getRound() {
		return round;
	}
}
