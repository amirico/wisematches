package wisematches.playground.tourney.regular.impl;

import wisematches.personality.Language;
import wisematches.playground.tourney.regular.TourneySection;
import wisematches.playground.tourney.regular.TourneySubscription;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tourney_regular_subs")
@NamedNativeQueries({
		@NamedNativeQuery(name = "tourney.outOfSection",
				query = "select playerId " +
						"from scribble_statistic s, tourney_regular_subs t " +
						"where s.playerId=t.player and t.tourneyNumber=:tourney and t.roundNumber=:round and t.language=:language and t.section=:section and s.rating>:rating",
				resultSetMapping = "playerIds")
})
@SqlResultSetMappings({
		@SqlResultSetMapping(name = "playerIds", columns = {@ColumnResult(name = "playerId")})
})
public class HibernateTourneySubscription implements TourneySubscription {
	@EmbeddedId
	private Id id;

	@Column(name = "language")
	@Enumerated(EnumType.ORDINAL)
	private Language language;

	@Column(name = "section")
	@Enumerated(EnumType.ORDINAL)
	private TourneySection section;

	protected HibernateTourneySubscription() {
	}

	public HibernateTourneySubscription(long player, int tourney, int round, Language language, TourneySection section) {
		this.id = new Id(player, tourney, round);
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

		public Id(long player, int tourney, int round) {
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
