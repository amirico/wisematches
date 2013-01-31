package wisematches.playground.tourney.regular.impl;

import wisematches.core.Language;
import wisematches.playground.tourney.regular.RegistrationRecord;
import wisematches.playground.tourney.regular.TourneySection;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tourney_regular_subs")
public class HibernateRegistrationRecord implements RegistrationRecord {
	@EmbeddedId
	private Id id;

	@Column(name = "language")
	@Enumerated(EnumType.ORDINAL)
	private Language language;

	@Column(name = "section")
	@Enumerated(EnumType.ORDINAL)
	private TourneySection section;

	protected HibernateRegistrationRecord() {
	}

	public HibernateRegistrationRecord(int tourney, long player, Language language, TourneySection section) {
		this(tourney, player, 1, language, section);
	}

	public HibernateRegistrationRecord(int tourney, long player, int round, Language language, TourneySection section) {
		this.id = new Id(tourney, player, round);
		this.language = language;
		this.section = section;
	}

	@Override
	public long getPlayer() {
		return id.player;
	}

	@Override
	public int getTourney() {
		return id.tourney;
	}

	@Override
	public long getRound() {
		return id.round;
	}

	@Override
	public Language getLanguage() {
		return language;
	}

	@Override
	public TourneySection getSection() {
		return section;
	}

	@Embeddable
	public static class Id implements Serializable {
		@Column(name = "roundNumber")
		private int round;

		@Column(name = "tourneyNumber")
		private int tourney;

		@Column(name = "player")
		private long player;

		public Id() {
		}

		public Id(int tourney, long player, int round) {
			this.tourney = tourney;
			this.round = round;
			this.player = player;
		}

		public int getRound() {
			return round;
		}

		public int getTourney() {
			return tourney;
		}

		public long getPlayer() {
			return player;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Id id = (Id) o;
			return player == id.player && round == id.round && tourney == id.tourney;
		}

		@Override
		public int hashCode() {
			int result = round;
			result = 31 * result + tourney;
			result = 31 * result + (int) (player ^ (player >>> 32));
			return result;
		}


		@Override
		public String toString() {
			return "Id{" +
					"round=" + round +
					", tourney=" + tourney +
					", player=" + player +
					'}';
		}
	}
}
