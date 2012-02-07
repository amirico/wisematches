package wisematches.playground.tournament.r1;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentGroup {
	/**
	 * Returns id of tournament's tour
	 *
	 * @return the id of tournament's tour
	 */
	long getTour();

	/**
	 * Returns the group's number
	 *
	 * @return the group's number
	 */
	int getNumber();
}
