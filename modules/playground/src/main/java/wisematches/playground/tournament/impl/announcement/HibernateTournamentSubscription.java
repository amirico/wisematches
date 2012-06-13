package wisematches.playground.tournament.impl.announcement;

import wisematches.personality.Language;
import wisematches.personality.player.Player;
import wisematches.playground.tournament.TournamentSubscription;
import wisematches.playground.tournament.TournamentCategory;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tournament_request")
public class HibernateTournamentSubscription implements TournamentSubscription {
	@EmbeddedId
	private PK pk;

	@Column(name = "section")
	@Enumerated(EnumType.ORDINAL)
	private TournamentCategory category;

	@Deprecated
	private HibernateTournamentSubscription() {
	}

	HibernateTournamentSubscription(int tournament, long player, Language language, TournamentCategory category) {
		if (language == null) {
			throw new NullPointerException("Language cant be null");
		}
		if (tournamentCategory == null) {
			throw new NullPointerException("Section cant be null");
		}
		this.pk = new PK(tournament, player, language);
		this.tournamentCategory = category;
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
	public TournamentCategory getTournamentCategory() {
		return category;
	}

	void setTournamentSection(TournamentCategory category) {
		if (tournamentCategory == null) {
			throw new NullPointerException("Section cant be null");
		}
		this.tournamentCategory = category;
	}

	PK getPk() {
		return pk;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof HibernateTournamentSubscription)) return false;

		HibernateTournamentSubscription that = (HibernateTournamentSubscription) o;
		return pk.equals(that.pk) && category == that.tournamentCategory;
	}

	@Override
	public int hashCode() {
		int result = pk.hashCode();
		result = 31 * result + category.hashCode();
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("HibernateAnnouncementRequest");
		sb.append("{pk=").append(pk);
		sb.append(", section=").append(tournamentCategory);
		sb.append('}');
		return sb.toString();
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

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("PK");
			sb.append("{announcement=").append(announcement);
			sb.append(", player=").append(player);
			sb.append(", language=").append(language);
			sb.append('}');
			return sb.toString();
		}
	}
}
