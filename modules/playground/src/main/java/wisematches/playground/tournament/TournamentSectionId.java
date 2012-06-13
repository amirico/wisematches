package wisematches.playground.tournament;

import wisematches.personality.Language;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class TournamentSectionId extends TournamentEntityId {
	private final Language language;
	private final TournamentCategory category;

	private static final long serialVersionUID = -6682183322507770407L;

	private TournamentSectionId(int tournament, Language language, TournamentCategory category) {
		super(tournament);
		this.language = language;
		this.category = category;
	}

	public Language getLanguage() {
		return language;
	}

	public TournamentCategory getTournamentCategory() {
		return category;
	}

	public static TournamentSectionId valueOf(int tournament, Language language, TournamentCategory category) {
		if (language == null) {
			throw new IllegalArgumentException("Language can't be null");
		}
		if (category == null) {
			throw new IllegalArgumentException("Section can't be null");
		}
		return new TournamentSectionId(tournament, language, category);
	}

	public static TournamentSectionId valueOf(Tournament tournament, Language language, TournamentCategory category) {
		return valueOf(tournament.getNumber(), language, category);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TournamentSectionId sectionId = (TournamentSectionId) o;
		return tournament == sectionId.tournament && language == sectionId.language && category == sectionId.category;
	}

	@Override
	public int hashCode() {
		int result = tournament;
		result = 31 * result + language.hashCode();
		result = 31 * result + category.hashCode();
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("TournamentSectionId");
		sb.append("{tournament=").append(tournament);
		sb.append(", language=").append(language);
		sb.append(", section=").append(category);
		sb.append('}');
		return sb.toString();
	}
}
