package wisematches.playground.tournament;

import wisematches.personality.Language;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class TournamentGroupId extends TournamentEntityId {
	private final TournamentRoundId roundId;
	private final int group;

	private static final long serialVersionUID = 7158968322456519933L;

	private TournamentGroupId(TournamentRoundId roundId, int group) {
		super(roundId.tournament);
		this.roundId = roundId;
		this.group = group;
	}

	public static TournamentGroupId valueOf(TournamentRoundId roundId, int group) {
		if (roundId == null) {
			throw new IllegalArgumentException("Round id can't be null");
		}
		return new TournamentGroupId(roundId, group);
	}

	public static TournamentGroupId valueOf(int tournament, Language language, TournamentCategory category, int round, int group) {
		return valueOf(TournamentRoundId.valueOf(tournament, language, category, round), group);
	}

	public static TournamentGroupId valueOf(Tournament tournament, Language language, TournamentCategory category, int round, int group) {
		return valueOf(TournamentRoundId.valueOf(tournament, language, category, round), group);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TournamentGroupId groupId = (TournamentGroupId) o;
		return group == groupId.group && roundId.equals(groupId.roundId);
	}

	@Override
	public int hashCode() {
		int result = roundId.hashCode();
		result = 31 * result + group;
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("TournamentGroupId");
		sb.append("{roundId=").append(roundId);
		sb.append(", group=").append(group);
		sb.append('}');
		return sb.toString();
	}
}