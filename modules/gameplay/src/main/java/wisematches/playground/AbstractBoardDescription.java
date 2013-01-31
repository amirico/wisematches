package wisematches.playground;

import wisematches.core.Personality;

import javax.persistence.MappedSuperclass;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@MappedSuperclass
public class AbstractBoardDescription<S extends GameSettings, H extends AbstractPlayerHand> implements BoardDescription<S, H> {
	public AbstractBoardDescription() {
	}

	@Override
	public long getBoardId() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public S getSettings() {
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
	public H getPlayerHand(Personality player) {
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
