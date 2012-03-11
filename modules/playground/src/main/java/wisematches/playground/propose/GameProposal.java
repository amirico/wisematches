package wisematches.playground.propose;

import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.playground.GameSettings;
import wisematches.playground.ViolatedRestrictionException;

import java.util.Collection;
import java.util.Date;

/**
 * The base interface that contains information about game proposal.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameProposal<S extends GameSettings> {
    /**
     * Returns proposal id
     *
     * @return the id of this proposal.
     */
    long getId();

    /**
     * Returns game settings for this proposal.
     *
     * @return the game settings
     */
    S getGameSettings();

    /**
     * Returns total number of players
     *
     * @return the total number of players
     */
    int getPlayersCount();

    /**
     * Returns date when the proposal was created.
     *
     * @return the date when the proposal was created.
     */
    Date getInitiationDate();

    /**
     * Returns a player who has initiated the proposal.
     *
     * @return the proposal's initiator.
     */
    Personality getInitiator();

    /**
     * Returns collection of player who already joined to the game
     *
     * @return the unmodifiable collection of player who already joined to the game
     */
    Collection<Personality> getPlayers();

    /**
     * Checks is this proposal contains specified player or not.
     *
     * @param personality the player to be checked
     * @return {@code true} if proposal has specified player; {@code false} - otherwise.
     */
    boolean containsPlayer(Personality personality);

    /**
     * Checks is specified player suitable and can't be added to this proposal. The method doesn't return
     * anything in case if player is correct; otherwise {@code }
     *
     * @param player the player to be checked.
     * @throws ViolatedRestrictionException if player can't be added to the proposal.
     * @see #getGameRestrictions()
     * @deprecated proposal is just description. All check should be done inside {@code GameProposalManager class}
     */
    @Deprecated
    void isSuitablePlayer(Player player) throws ViolatedRestrictionException;

    /**
     * Checks is specified proposal is ready or not. Proposal is ready if number of waiting players equals to
     * number of joined players.
     *
     * @return returns {@code true} if proposal is ready and new game can be created; {@code false} - otherwise.
     */
    boolean isReady();
}
