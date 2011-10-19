package wisematches.playground.propose.impl;

import wisematches.personality.player.Player;
import wisematches.playground.GameSettings;
import wisematches.playground.propose.GameRestriction;
import wisematches.playground.propose.ViolatedRestrictionException;
import wisematches.playground.propose.WaitingGameProposal;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class DefaultWaitingGameProposal<S extends GameSettings> extends AbstractGameProposal<S> implements WaitingGameProposal<S> {
	private final GameRestriction gameRestriction;

	private static final long serialVersionUID = -7397713360246859828L;

	public DefaultWaitingGameProposal(long id, S gameSettings, Player initiator, int playersCount) {
		this(id, gameSettings, playersCount, initiator, null);
	}

	public DefaultWaitingGameProposal(long id, S gameSettings, int playersCount, Player initiator, GameRestriction gameRestriction) {
		super(id, gameSettings, playersCount, initiator);
		this.gameRestriction = gameRestriction;
	}

	@Override
	protected void validateRestrictions(Player player) throws ViolatedRestrictionException {
		if (gameRestriction != null) {
			gameRestriction.validatePlayer(player);
		}
	}

	@Override
	public GameRestriction getRestriction() {
		return gameRestriction;
	}
}
