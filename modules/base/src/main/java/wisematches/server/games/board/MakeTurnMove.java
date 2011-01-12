package wisematches.server.games.board;

/**
 * Predefined move that indicates that move is maden and not passed.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class MakeTurnMove extends PlayerMove {
	public MakeTurnMove(long gamePlayer) {
		super(gamePlayer);
	}
}
