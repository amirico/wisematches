/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.gameplaying.tournament;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 * @version $Id:  $
 */
public interface TournamentListener {
	void tournamentAnnounce(Tournament tournament);

	void tournamentStarted(Tournament tournament);

	void tournamentFinished(Tournament tournament);

	void tournamentRoundStarted(TournamentRound round);
}
