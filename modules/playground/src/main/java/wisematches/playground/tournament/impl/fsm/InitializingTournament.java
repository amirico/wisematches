package wisematches.playground.tournament.impl.fsm;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class InitializingTournament implements TSMActivityContext {
	private final int number;

	public InitializingTournament(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}
}
