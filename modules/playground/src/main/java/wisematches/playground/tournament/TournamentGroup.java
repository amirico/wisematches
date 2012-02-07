package wisematches.playground.tournament;

/**
 * The tournament group is last tournament entity that describes players and games for one group.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentGroup {
	/**
	 * Returns tournament for the group.
	 *
	 * @return the tournament for the group.
	 */
	int getTournament();

	/**
	 * Returns round fot the group.
	 *
	 * @return the round fot the group.
	 */
	int getRound();

	/**
	 * Returns number of the group.
	 *
	 * @return the number of the group.
	 */
	int getNumber();

	/**
	 * Returns array of players in the group.
	 *
	 * @return the array of players in the group.
	 */
	long[] getPlayers();

	/**
	 * Returns points for finished games where index in the array the number of a game in the following matrix:
	 * <pre>
	 *     	| p1 | p2 | p3 | p4 |
	 *   p2 | 1  |    |    |    |
	 *   p3 | 2  | 5  |    |    |
	 *   p4 | 3  | 6  | 8  |    |
	 *   p5 | 4  | 7  | 9  | 10 |
	 * </pre>
	 * <p/>
	 * or by the following common formula:
	 * <pre>
	 *     index=n*(m-1)+k-m+S(m-1)
	 * </pre>
	 * where:
	 * <pre>
	 *     		n - number of players
	 *     		m - column index; 1<=m<n
	 *     		k - row index; 1<k<=n
	 *     		S(m-1) - arithmetic progression of m-1 elements
	 * </pre>
	 *
	 * @return
	 */
	int[] getPoints();

	/**
	 * Returns game number for games where index in the array the number of a game in the following matrix:
	 * <pre>
	 *     	| p1 | p2 | p3 | p4 |
	 *   p2 | 1  |    |    |    |
	 *   p3 | 2  | 5  |    |    |
	 *   p4 | 3  | 6  | 8  |    |
	 *   p5 | 4  | 7  | 9  | 10 |
	 * </pre>
	 * <p/>
	 * or by the following common formula:
	 * <pre>
	 *     index=n*(m-1)+k-m+S(m-1)
	 * </pre>
	 * where:
	 * <pre>
	 *     		n - number of players
	 *     		m - column index; 1<=m<n
	 *     		k - row index; 1<k<=n
	 *     		S(m-1) - arithmetic progression of m-1 elements
	 * </pre>
	 *
	 * @return
	 */
	long[] getGames();
}