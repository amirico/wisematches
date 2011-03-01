package wisematches.server.gameplaying.room.propose;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameProposalListener {
	void gameProposalInitiated(GameProposal proposal);

	void gameProposalUpdated(GameProposal proposal);

	void gameProposalClosed(GameProposal proposal);
}
