/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */
package wisematches.playground.tournament.refactoring;

/**
 * {@code TournamentListener} allows get notifications about future tournaments and it's states.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentListener {
	/**
	 * Indicates that new tournament has been announced. New tournament usually announced
	 * just after current one was started.
	 *
	 * @param tournament the tournament that was announced.
	 */
	void tournamentAnnounce(Tournament tournament);

	/**
	 * Indicates that new tournament round (and first one) has been started.
	 *
	 * @param round the round that has been started.
	 */
	void tournamentNextRound(TournamentRound round);

	/**
	 * Indicates that tournament has been finished.
	 *
	 * @param tournament tournament that has been finishied.
	 */
	void tournamentFinished(Tournament tournament);
}
