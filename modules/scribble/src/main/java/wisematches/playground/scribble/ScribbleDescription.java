package wisematches.playground.scribble;

import wisematches.core.Personality;
import wisematches.playground.BoardDescription;
import wisematches.playground.GameRelationship;
import wisematches.playground.GameResolution;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleDescription implements BoardDescription<ScribbleSettings, ScribblePlayerHand> {
	public ScribbleDescription() {
	}

	@Override
	public long getBoardId() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public ScribbleSettings getSettings() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public boolean isRated() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public boolean isActive() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public Date getStartedTime() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public Date getFinishedTime() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public Date getLastMoveTime() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public Personality getPlayerTurn() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public int getPlayersCount() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public List<Personality> getPlayers() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public ScribblePlayerHand getPlayerHand(Personality player) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public GameResolution getResolution() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public GameRelationship getRelationship() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public Collection<Personality> getWonPlayers() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}
}
