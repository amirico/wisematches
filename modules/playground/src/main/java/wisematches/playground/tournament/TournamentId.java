package wisematches.playground.tournament;

import wisematches.personality.Language;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class TournamentId extends TournamentEntityId {
	private final Language language;

	private static final long serialVersionUID = 1729920011566125626L;

	public TournamentId(int tournament, Language language) {
		super(tournament);
		this.language = language;
	}

	public static TournamentId valueOf(int tournament, Language language) {
		if (language == null) {
			throw new IllegalArgumentException("Language can't be null");
		}
		return new TournamentId(tournament, language);
	}

	public static TournamentId valueOf(Tournament tournament, Language language) {
		return valueOf(tournament.getNumber(), language);
	}

	public Language getLanguage() {
		return language;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TournamentId that = (TournamentId) o;
		return language == that.language;
	}

	@Override
	public int hashCode() {
		return language.hashCode();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("TournamentId");
		sb.append("{language=").append(language);
		sb.append('}');
		return sb.toString();
	}
}
