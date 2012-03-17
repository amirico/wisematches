package wisematches.playground.propose;

/**
 * Contains type of a proposal.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum ProposalType {
	/**
	 * It's public proposal that is opened for all players and any player who meets required criteria can join.
	 */
	PUBLIC,

	/**
	 * It's challenge that was sent from one player to one or more other players.
	 */
	PRIVATE,
}
