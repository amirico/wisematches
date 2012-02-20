package wisematches.playground.tournament.upcoming.impl;

import wisematches.personality.Language;
import wisematches.personality.player.Player;
import wisematches.playground.tournament.TournamentSection;
import wisematches.playground.tournament.upcoming.TournamentRequest;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tournament_request")
public class HibernateTournamentRequest implements TournamentRequest {
	@EmbeddedId
	private PK pk;

	@Column(name = "section")
	@Enumerated(EnumType.ORDINAL)
	private TournamentSection section;

	private HibernateTournamentRequest() {
	}

	HibernateTournamentRequest(int tournament, long player, Language language, TournamentSection section) {
		this.pk = new PK(tournament, player, language);
		this.section = section;
	}

	@Override
	public long getPlayer() {
		return pk.getPlayer();
	}

	@Override
	public int getAnnouncement() {
		return pk.getAnnouncement();
	}

	@Override
	public Language getLanguage() {
		return pk.getLanguage();
	}

	@Override
	public TournamentSection getSection() {
		return section;
	}

	void setTournamentSection(TournamentSection section) {
		this.section = section;
	}

	@Embeddable
	static final class PK implements Serializable {
		@Column(name = "announcement")
		private int announcement;

		@Column(name = "player")
		private long player;

		@Column(name = "language")
		@Enumerated(EnumType.STRING)
		private Language language;

		private PK() {
		}

		public PK(int announcement, Player player, Language language) {
			this(announcement, player.getId(), language);
		}

		public PK(int announcement, long player, Language language) {
			this.announcement = announcement;
			this.player = player;
			this.language = language;
		}

		public int getAnnouncement() {
			return announcement;
		}

		public long getPlayer() {
			return player;
		}

		public Language getLanguage() {
			return language;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			PK PK = (PK) o;
			return announcement == PK.announcement && player == PK.player && language == PK.language;
		}

		@Override
		public int hashCode() {
			int result = announcement;
			result = 31 * result + (int) (player ^ (player >>> 32));
			result = 31 * result + language.hashCode();
			return result;
		}
	}
}
