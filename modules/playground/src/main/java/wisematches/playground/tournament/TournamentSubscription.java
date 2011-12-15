/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.playground.tournament;

import wisematches.personality.Language;

/**
 * This class contains information about subscription for player.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class TournamentSubscription {
	private long playerId;
	private Language language;
	private TournamentSection tournamentSection;

	public TournamentSubscription(long playerId, Tournament tournament, TournamentSection tournamentSection, Language language) {
		if (tournament == null) {
			throw new NullPointerException("Tournament can't be null");
		}
		if (tournamentSection == null) {
			throw new NullPointerException("TournamentSection can't be null");
		}
		if (language == null) {
			throw new NullPointerException("Language can't be null");
		}
		this.playerId = playerId;
		this.tournamentSection = tournamentSection;
		this.language = language;
	}

	public long getPlayerId() {
		return playerId;
	}

	public Language getLanguage() {
		return language;
	}

	public TournamentSection getSection() {
		return tournamentSection;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TournamentSubscription that = (TournamentSubscription) o;
		return playerId == that.playerId && language == that.language && tournamentSection == that.tournamentSection;
	}

	@Override
	public int hashCode() {
		int result = (int) (playerId ^ (playerId >>> 32));
		result = 31 * result + language.hashCode();
		result = 31 * result + tournamentSection.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "TournamentSubscription{" +
				"playerId=" + playerId +
				", language=" + language +
				", tournamentSection=" + tournamentSection +
				'}';
	}
}
