package wisematches.playground.tournament;

import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class TournamentEntityId implements Serializable {
	protected final int tournament;

	TournamentEntityId(int tournament) {
		this.tournament = tournament;
	}

	public int getTournament() {
		return tournament;
	}

	@Override
	public abstract boolean equals(Object o);

	@Override
	public abstract int hashCode();

	@Override
	public abstract String toString();
}
