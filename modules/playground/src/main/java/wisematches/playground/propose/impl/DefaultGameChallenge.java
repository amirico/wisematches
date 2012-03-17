package wisematches.playground.propose.impl;

import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.playground.GameSettings;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultGameChallenge<S extends GameSettings> extends AbstractGameProposal<S> implements GameChallenge<S> {
    private final String comment;
    private final Collection<Personality> waitingPlayers = new HashSet<Personality>();

    private static final long serialVersionUID = -3407502628388362362L;

    public DefaultGameChallenge(long id, S gameSettings, String comment, Player initiator, Collection<Player> waiting) {
        super(id, gameSettings, 1 + waiting.size(), initiator);
        this.comment = comment;
        for (Player player : waiting) {
            if (player == null) {
                throw new NullPointerException("error.proposal.null.player");
            }
            if (!waitingPlayers.add(Personality.untie(player)) || player.equals(initiator)) {
                throw new IllegalArgumentException("error.proposal.twice.player");
            }
        }
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public Collection<Personality> getWaitingPlayers() {
        return Collections.unmodifiableCollection(waitingPlayers);
    }

    @Override
    protected void validateRestrictions(Player player) throws ViolatedCriterionException {
        boolean expected = false;
        for (Personality restriction : waitingPlayers) {
            expected |= restriction.equals(player);
        }

        if (!expected) {
            throw new ViolatedCriterionException("player.unsuitable");
        }
    }
}