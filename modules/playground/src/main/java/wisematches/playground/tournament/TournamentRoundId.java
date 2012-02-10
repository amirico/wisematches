package wisematches.playground.tournament;

import wisematches.personality.Language;

import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class TournamentRoundId implements Serializable {
	private final TournamentSectionId sectionId;
	private final int round;

	private static final long serialVersionUID = -6236607047654913384L;

	private TournamentRoundId(TournamentSectionId sectionId, int round) {
		this.sectionId = sectionId;
		this.round = round;
	}

	public TournamentSectionId getSectionId() {
		return sectionId;
	}

	public int getRound() {
		return round;
	}

	public static TournamentRoundId valueOf(TournamentSectionId sectionId, int round) {
		if (sectionId == null) {
			throw new IllegalArgumentException("Section id can't be null");
		}
		return new TournamentRoundId(sectionId, round);
	}

	public static TournamentRoundId valueOf(int tournament, Language language, TournamentSection section, int round) {
		return valueOf(TournamentSectionId.valueOf(tournament, language, section), round);
	}

	public static TournamentRoundId valueOf(Tournament tournament, Language language, TournamentSection section, int round) {
		return valueOf(TournamentSectionId.valueOf(tournament, language, section), round);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TournamentRoundId roundId = (TournamentRoundId) o;
		return round == roundId.round && sectionId.equals(roundId.sectionId);
	}

	@Override
	public int hashCode() {
		int result = sectionId.hashCode();
		result = 31 * result + round;
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("TournamentRoundId");
		sb.append("{sectionId=").append(sectionId);
		sb.append(", round=").append(round);
		sb.append('}');
		return sb.toString();
	}
}
