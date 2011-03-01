package wisematches.server.gameplaying.room.propose;

import wisematches.server.player.Player;

import java.util.Collection;

/**
 * {@code WaitingGameManager} provide access to waiting games information. Waiting games don't have a board and can
 * be managed via this manager.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameProposalManager<P extends GameProposal> {
	void addGameProposalListener(GameProposalListener l);

	void removeGameProposalListener(GameProposalListener l);

	/**
	 * Opens new waiting game in this manager.
	 *
	 * @param proposal the waiting game info.
	 */
	void initiateGameProposal(P proposal);

	/**
	 * Attaches specified player to the waiting game.
	 * <p/>
	 * If game is marked as a ready after attachment the waiting info
	 * is removed from the manager so the returned waiting info must be checked after this method execution.
	 *
	 * @param proposalId the waiting game id
	 * @param player	 the player to be added.
	 * @return the modified waiting game info.
	 */
	P attachPlayer(long proposalId, Player player);

	/**
	 * Detaches specified player from the waiting game.
	 *
	 * @param proposalId the waiting game id.
	 * @param player	 the player to be attached.
	 * @return the modified waiting game info.
	 */
	P detachPlayer(long proposalId, Player player);

	/**
	 * Close specified waiting game.
	 *
	 * @param proposal the waiting game to be closed.
	 */
	void closeGameProposal(P proposal);

	/**
	 * Returns list of all waiting games.
	 *
	 * @return the list of all waiting games.
	 */
	Collection<P> getActiveProposals();
}