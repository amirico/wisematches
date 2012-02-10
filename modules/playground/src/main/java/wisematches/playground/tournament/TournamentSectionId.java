package wisematches.playground.tournament;

import wisematches.personality.Language;

import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class TournamentSectionId implements Serializable {
	private final int tournament;
	private final Language language;
	private final TournamentSection section;

	private static final long serialVersionUID = -6682183322507770407L;

	private TournamentSectionId(int tournament, Language language, TournamentSection section) {
		this.tournament = tournament;
		this.language = language;
		this.section = section;
	}

	public int getTournament() {
		return tournament;
	}

	public Language getLanguage() {
		return language;
	}

	public TournamentSection getSection() {
		return section;
	}

	public static TournamentSectionId valueOf(int tournament, Language language, TournamentSection section) {
		if (language == null) {
			throw new IllegalArgumentException("Language can't be null");
		}
		if (section == null) {
			throw new IllegalArgumentException("Section can't be null");
		}
		return new TournamentSectionId(tournament, language, section);
	}

	public static TournamentSectionId valueOf(Tournament tournament, Language language, TournamentSection section) {
		return valueOf(tournament.getNumber(), language, section);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TournamentSectionId sectionId = (TournamentSectionId) o;

		return tournament == sectionId.tournament && language == sectionId.language && section == sectionId.section;
	}

	@Override
	public int hashCode() {
		int result = tournament;
		result = 31 * result + language.hashCode();
		result = 31 * result + section.hashCode();
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("TournamentSectionId");
		sb.append("{tournament=").append(tournament);
		sb.append(", language=").append(language);
		sb.append(", section=").append(section);
		sb.append('}');
		return sb.toString();
	}
}
