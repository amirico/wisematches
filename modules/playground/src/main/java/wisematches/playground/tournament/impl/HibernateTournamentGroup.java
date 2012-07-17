package wisematches.playground.tournament.impl;

import wisematches.personality.Language;
import wisematches.playground.tournament.TournamentGroup;
import wisematches.playground.tournament.TournamentSection;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tournament_group")
public class HibernateTournamentGroup extends TournamentGroup {
	@EmbeddedId
	private PK pk;

	@Column(name = "player1")
	private long player1;

	@Column(name = "player2")
	private long player2;

	@Column(name = "player3")
	private long player3;

	@Column(name = "player4")
	private long player4;

	private HibernateTournamentGroup() {
	}

	HibernateTournamentGroup(int tournament, int round, long[] players) {
		pk = new PK(tournament, round, 1);
	}

	@Override
	public int getTournament() {
		return pk.tournament;
	}

	@Override
	public Language getLanguage() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public TournamentSection getSection() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public Date getStartedDate() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public Date getFinishedDate() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public int getTotalGamesCount() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public int getFinishedGamesCount() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public int getRound() {
		return pk.round;
	}

	@Override
	public int getGroup() {
		return pk.number;
	}

	@Override
	public long[] getPlayers() {
		return new long[]{player1, player2, player3, player4};
	}

	@Override
	public long getGame(long p1, long p2) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public short getScores(long player) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public short getPoints(long p1, long p2) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Embeddable
	static final class PK implements Serializable {
		@Column(name = "tournament")
		private int tournament;

		@Column(name = "round")
		private int round;

		@Column(name = "number")
		private int number;

		PK() {
		}

		PK(int tournament, int round, int number) {
			this.tournament = tournament;
			this.round = round;
			this.number = number;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof PK)) return false;

			PK pk = (PK) o;
			return number == pk.number && round == pk.round && tournament == pk.tournament;
		}

		@Override
		public int hashCode() {
			int result = tournament;
			result = 31 * result + round;
			result = 31 * result + number;
			return result;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("PK");
			sb.append("{tournament=").append(tournament);
			sb.append(", round=").append(round);
			sb.append(", number=").append(number);
			sb.append('}');
			return sb.toString();
		}
	}
}
