package wisematches.playground.tournament.impl.fsm;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TSMInitializingTournament implements TSMActivityContext {
	private final int number;

	public TSMInitializingTournament(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}
}
