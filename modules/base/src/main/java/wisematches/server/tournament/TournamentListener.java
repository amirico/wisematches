/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.tournament;

/**
 * @author klimese
 * @version $Id:  $
 */
public interface TournamentListener {
	void tournamentAnnounce(Tournament tournament);

	void tournamentStarted(Tournament tournament);

	void tournamentFinished(Tournament tournament);

	void tournamentRoundStarted(TournamentRound round);
}
