package wisematches.playground.propose;

/**
 * Search context for {@code GameProposalManager}.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum ProposalRelation {
	/**
	 * The {@code GameProposalManager} will return only proposals with specified players. If player is {@code null}
	 * when empty list will be returned.
	 */
	INVOLVED,

	/**
	 * The {@code GameProposalManager} will return all available proposals.
	 */
	AVAILABLE
}
