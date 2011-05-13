package wisematches.playground;

import wisematches.server.personality.Personality;

/**
 * This exception is throw if player can't make a turn because turn belongs to other player or player
 * doesn't belong to this game.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class UnsuitablePlayerException extends IncorrectMoveException {
	private final long expectedPlayerId;
	private final long specifiedPlayerId;

	/**
	 * Creates new exception with specified operation name and player who try do this operation.
	 * <p/>
	 * This constructor is used to indicates that player does not belongs to game and can not
	 * perform required operation.
	 *
	 * @param operation the doing operation.
	 * @param player	the player who try do this operation.
	 */
	public UnsuitablePlayerException(String operation, Personality player) {
		super("Player " + "[" + player.getId() + "] can not perform " +
				operation + " operation because does not belongs to this game.");

		expectedPlayerId = 0;
		specifiedPlayerId = player.getId();
	}

	/**
	 * Creates new exception with specified operation name and player who try do this operation.
	 * <p/>
	 * This constructor is used to indicates that player does not belongs to game and can not
	 * perform required operation.
	 *
	 * @param operation the doing operation.
	 * @param playerId  the player id who try do this operation.
	 */
	public UnsuitablePlayerException(String operation, long playerId) {
		super("Player with id " + playerId + " can not perform " +
				operation + " operation because does not belongs to this game.");

		expectedPlayerId = 0;
		specifiedPlayerId = playerId;
	}

	/**
	 * Creates new exception with specified operation name, player who excepted and player who try do this operation.
	 * <p/>
	 * This constructor is used to indicates that one player try perform some operation but
	 * another player is expected.
	 *
	 * @param operation	   the doing operation.
	 * @param expectedPlayer  the player who expected.
	 * @param specifiedPlayer the player who try to perform operation.
	 */
	public UnsuitablePlayerException(String operation, Personality expectedPlayer, Personality specifiedPlayer) {
		super("For " + operation + " operation player " + "[" + expectedPlayer.getId() + "]" +
				" expected but " + "player " + "[" + specifiedPlayer.getId() + "]" + " was specified");
		this.expectedPlayerId = expectedPlayer.getId();
		this.specifiedPlayerId = specifiedPlayer.getId();
	}

	/**
	 * Creates new exception with specified operation name, player who excepted and player who try do this operation.
	 * <p/>
	 * This constructor is used to indicates that one player try perform some operation but
	 * another player is expected.
	 *
	 * @param operation		 the doing operation.
	 * @param expectedPlayerId  the player id who expected.
	 * @param specifiedPlayerId the player id who try to perform operation.
	 */
	public UnsuitablePlayerException(String operation, long expectedPlayerId, long specifiedPlayerId) {
		super("For " + operation + " operation player with id " + expectedPlayerId + " expected but " +
				"player with id " + specifiedPlayerId + " was specified");
		this.expectedPlayerId = expectedPlayerId;
		this.specifiedPlayerId = specifiedPlayerId;
	}

	/**
	 * Returns id of expected player. This method returns 0 (zero) if specified player does not
	 * belongs to game and can not perform required operation at all.
	 *
	 * @return the id of player that is expected. Can be 0 (zero) if specified player does not
	 *         belongs to game and can not perform required operation at all
	 */
	public long getExpectedPlayerId() {
		return expectedPlayerId;
	}

	/**
	 * Returns id of speciied player who try perform some operation.
	 *
	 * @return the id of player who try do somethind.
	 */
	public long getSpecifiedPlayerId() {
		return specifiedPlayerId;
	}
}
