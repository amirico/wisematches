package wisematches.server.gameplaying.propose;

import wisematches.server.gameplaying.board.GameSettings;
import wisematches.server.personality.Personality;

import java.util.Collection;

/**
 * {@code GameProposalManager} provide access to waiting games information.
 * <p/>
 * Waiting games don't have a board and can be managed via this manager only.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameProposalManager<S extends GameSettings> {
    void addGameProposalListener(GameProposalListener l);

    void removeGameProposalListener(GameProposalListener l);

    /**
     * Opens new waiting game in this manager.
     *
     * @param settings     the original game settings
     * @param playersCount the max players count
     * @param players      current players count. List must contains at least one player (initiator)
     * @return returns the proposal (possible new or modified).
     * @throws IllegalArgumentException if {@code settings} or {@code players} are null or if {@code playersCount}
     *                                  is less than two or if {@code players} collection size more than
     *                                  {@code playersCount} or contains null.
     */
    GameProposal<S> initiateGameProposal(S settings, int playersCount, Collection<Personality> players);

    /**
     * Attaches specified player to the waiting game.
     * <p/>
     * If game is marked as a ready after attachment the waiting info is removed from the manager so the returned
     * waiting info must be checked after this method execution.
     *
     * @param proposalId the waiting game id
     * @param player     the player to be added.
     * @return the modified waiting game info or {@code null} if no proposal with specified id.
     * @throws IllegalArgumentException if specified player is null.
     */
    GameProposal<S> attachPlayer(long proposalId, Personality player);

    /**
     * Detaches specified player from the waiting game.
     *
     * @param proposalId the waiting game id.
     * @param player     the player to be attached.
     * @return the modified waiting game info or {@code null} if no proposal with specified id.
     * @throws IllegalArgumentException if specified player is null.
     */
    GameProposal<S> detachPlayer(long proposalId, Personality player);

    /**
     * Returns list of all waiting games.
     *
     * @return the list of all waiting games.
     */
    Collection<GameProposal<S>> getActiveProposals();

    /**
     * Returns list of all active proposals for specified player
     *
     * @param player the player who's proposals should be returned
     * @return unmodifiable list of all active player's proposals.
     */
    Collection<GameProposal<S>> getPlayerProposals(Personality player);
}