/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.gameplaying.tournament.subscription;

import wisematches.server.gameplaying.tournament.Tournament;
import wisematches.server.gameplaying.tournament.TournamentSection;
import wisematches.server.player.Language;

/**
 * This class contains information about subscription for player.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class TournamentSubscription {
	private long id;
	private long playerId;
	private Language language;
	private Tournament tournament;
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
		this.tournament = tournament;
		this.tournamentSection = tournamentSection;
		this.language = language;
	}

	public long getPlayerId() {
		return playerId;
	}

	public Language getLanguage() {
		return language;
	}

	public Tournament getTournament() {
		return tournament;
	}

	public TournamentSection getSectionType() {
		return tournamentSection;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TournamentSubscription that = (TournamentSubscription) o;

		if (playerId != that.playerId) return false;
		if (language != that.language) return false;
		if (tournamentSection != that.tournamentSection) return false;
		if (!tournament.equals(that.tournament)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = (int) (playerId ^ (playerId >>> 32));
		result = 31 * result + language.hashCode();
		result = 31 * result + tournament.hashCode();
		result = 31 * result + tournamentSection.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "TournamentSubscription{" +
				"playerId=" + playerId +
				", language=" + language +
				", tournament=" + tournament +
				", tournamentSection=" + tournamentSection +
				'}';
	}
}
