package wisematches.playground.tournament.impl;

import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.playground.tournament.Tournament;
import wisematches.playground.tournament.TournamentSection;
import wisematches.playground.tournament.TournamentSubscription;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tournament_subscription")
class HibernateTournamentSubscription implements TournamentSubscription {
	@EmbeddedId
	private PK subscriptionId;

	@Column(name = "section")
	@Enumerated(EnumType.ORDINAL)
	private TournamentSection section;

	HibernateTournamentSubscription(Tournament tournament, Personality player, Language language, TournamentSection section) {
		subscriptionId = new PK(tournament, player, language);
		this.section = section;
	}

	long getPlayerId() {
		return subscriptionId.getPlayerId();
	}

	int getTournamentId() {
		return subscriptionId.getTournamentId();
	}

	@Override
	public Language getLanguage() {
		return subscriptionId.getLanguage();
	}

	@Override
	public TournamentSection getSection() {
		return section;
	}

	public void setSection(TournamentSection section) {
		this.section = section;
	}

	@Embeddable
	protected static class PK implements Serializable {
		@Column(name = "tournament")
		private int tournamentId;

		@Column(name = "player")
		private long playerId;

		@Column(name = "language")
		@Enumerated(EnumType.STRING)
		private Language language;

		public PK(Tournament tournament, Personality player, Language language) {
			this.tournamentId = tournament.getNumber();
			this.playerId = player.getId();
			this.language = language;
		}

		public int getTournamentId() {
			return tournamentId;
		}

		public long getPlayerId() {
			return playerId;
		}

		public Language getLanguage() {
			return language;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			PK pk = (PK) o;
			return playerId == pk.playerId && tournamentId == pk.tournamentId && language == pk.language;
		}

		@Override
		public int hashCode() {
			int result = tournamentId;
			result = 31 * result + (int) (playerId ^ (playerId >>> 32));
			result = 31 * result + language.hashCode();
			return result;
		}
	}
}
