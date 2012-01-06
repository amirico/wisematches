package wisematches.playground.propose;

import wisematches.personality.Personality;
import wisematches.playground.GameSettings;

import java.util.Collection;

/**
 * Challenge game proposal is created for exist players.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface ChallengeGameProposal<S extends GameSettings> extends GameProposal<S> {
    /**
     * Returns comment for the challenge.
     *
     * @return the comment for the challenge.
     */
    String getComment();

    /**
     * Returns unmodifiable collection of waiting players.
     *
     * @return the unmodifiable collection of waiting players.
     */
    Collection<Personality> getWaitingPlayers();
}
