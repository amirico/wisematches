package wisematches.playground.propose.impl;

import wisematches.core.Player;
import wisematches.playground.GameSettings;
import wisematches.playground.propose.GameProposal;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractGameProposal<S extends GameSettings> implements GameProposal<S>, Serializable {
	private final long id;
	private final S settings;
	private final Date creationDate;

	protected final Player initiator;

	protected AbstractGameProposal(long id, S settings, Player initiator) {
		if (settings == null) {
			throw new NullPointerException("Settings can't be null");
		}
		if (initiator == null) {
			throw new NullPointerException("Initiator can't be null");
		}
		this.id = id;
		this.settings = settings;
		this.creationDate = new Date();
		this.initiator = initiator;
	}

	@Override
	public final long getId() {
		return id;
	}

	@Override
	public final S getSettings() {
		return settings;
	}

	@Override
	public final Date getCreationDate() {
		return creationDate;
	}

	@Override
	public final Player getInitiator() {
		return initiator;
	}

	protected abstract void attach(Player player);

	protected abstract void detach(Player player);
}
