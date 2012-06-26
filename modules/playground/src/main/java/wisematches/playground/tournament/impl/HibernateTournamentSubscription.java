package wisematches.playground.tournament.impl;

import wisematches.personality.Language;
import wisematches.personality.player.Player;
import wisematches.playground.tournament.TournamentSection;
import wisematches.playground.tournament.TournamentSubscription;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tournament_subscription")
public class HibernateTournamentSubscription implements TournamentSubscription, Serializable {
	@EmbeddedId
	private PK pk;

	@Column(name = "section")
	@Enumerated(EnumType.ORDINAL)
	private TournamentSection section;

	private static final long serialVersionUID = -4513139952040944066L;

	@Deprecated
	private HibernateTournamentSubscription() {
	}

	HibernateTournamentSubscription(int tournament, long player, Language language, TournamentSection section) {
		if (language == null) {
			throw new NullPointerException("Language cant be null");
		}
		if (section == null) {
			throw new NullPointerException("TournamentSection cant be null");
		}
		this.pk = new PK(tournament, player, language);
		this.section = section;
	}

	@Override
	public long getPlayer() {
		return pk.getPlayer();
	}

	@Override
	public int getTournament() {
		return pk.getTournament();
	}

	@Override
	public Language getLanguage() {
		return pk.getLanguage();
	}

	@Override
	public TournamentSection getSection() {
		return section;
	}

	void setTournamentSection(TournamentSection category) {
		if (section == null) {
			throw new NullPointerException("TournamentSection cant be null");
		}
		this.section = category;
	}

	PK getPk() {
		return pk;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof HibernateTournamentSubscription)) return false;

		HibernateTournamentSubscription that = (HibernateTournamentSubscription) o;
		return pk.equals(that.pk) && section == that.section;
	}

	@Override
	public int hashCode() {
		int result = pk.hashCode();
		result = 31 * result + section.hashCode();
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("HibernateAnnouncementRequest");
		sb.append("{pk=").append(pk);
		sb.append(", section=").append(section);
		sb.append('}');
		return sb.toString();
	}

	@Embeddable
	static final class PK implements Serializable {
		@Column(name = "tournament")
		private int tournament;

		@Column(name = "player")
		private long player;

		@Column(name = "language")
		@Enumerated(EnumType.STRING)
		private Language language;

		private PK() {
		}

		public PK(int tournament, Player player, Language language) {
			this(tournament, player.getId(), language);
		}

		public PK(int tournament, long player, Language language) {
			this.tournament = tournament;
			this.player = player;
			this.language = language;
		}

		public int getTournament() {
			return tournament;
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
			return tournament == PK.tournament && player == PK.player && language == PK.language;
		}

		@Override
		public int hashCode() {
			int result = tournament;
			result = 31 * result + (int) (player ^ (player >>> 32));
			result = 31 * result + language.hashCode();
			return result;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("PK");
			sb.append("{tournament=").append(tournament);
			sb.append(", player=").append(player);
			sb.append(", language=").append(language);
			sb.append('}');
			return sb.toString();
		}
	}
}
