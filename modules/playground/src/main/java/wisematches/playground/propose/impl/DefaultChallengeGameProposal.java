package wisematches.playground.propose.impl;

import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.playground.GameSettings;
import wisematches.playground.propose.ChallengeGameProposal;
import wisematches.playground.propose.ViolatedRestrictionException;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultChallengeGameProposal<S extends GameSettings> extends AbstractGameProposal<S> implements ChallengeGameProposal<S> {
	private final Player initiator;
	private final Collection<Personality> waitingPlayers = new HashSet<Personality>();

	private static final long serialVersionUID = -3407502628388362362L;

	public DefaultChallengeGameProposal(long id, S gameSettings, Player initiator, Collection<Player> waiting) {
		super(id, gameSettings, 1 + waiting.size(), Collections.singleton(initiator));
		this.initiator = initiator;
		for (Player player : waiting) {
			if (player == null) {
				throw new NullPointerException("error.proposal.null.player");
			}
			if (!waitingPlayers.add(Personality.untie(player)) || player.equals(initiator)) {
				throw new IllegalArgumentException("error.proposal.twice.player");
			}
		}
	}

	public Player getInitiator() {
		return initiator;
	}

	public Collection<Personality> getWaitingPlayers() {
		return Collections.unmodifiableCollection(waitingPlayers);
	}

	@Override
	protected void validateRestrictions(Player player) throws ViolatedRestrictionException {
		boolean expected = false;
		for (Personality restriction : waitingPlayers) {
			expected |= restriction.equals(player);
		}

		if (!expected) {
			throw new ViolatedRestrictionException("player.unsuitable");
		}
	}
}