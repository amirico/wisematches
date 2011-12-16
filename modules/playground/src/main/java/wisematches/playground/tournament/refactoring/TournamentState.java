/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */
package wisematches.playground.tournament.refactoring;

/**
 * The status of the tournament.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public enum TournamentState {
	/**
	 * Indicates that tournament was announced but not started yet.
	 */
	ANNOUNCED,

	/**
	 * Indicates that tournament was started.
	 */
	ACTIVE,

	/**
	 * Indicates that tournament was finished.
	 */
	FINISHED
}
