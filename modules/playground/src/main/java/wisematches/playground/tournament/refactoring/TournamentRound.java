/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.playground.tournament.refactoring;

import java.util.Date;

/**
 * {@code TournamentRound} represents one round of a tournament.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface TournamentRound {
	/**
	 * Returns global number of this round. Global number can be used to external references (for example, from
	 * {@code GameBoard} class).
	 *
	 * @return the global number of this tournament.
	 */
	long getGlobalNumber();

	/**
	 * Returns sequence number of this round starting with one.
	 *
	 * @return the sequence number of this round.
	 */
	int getSequenceNumber();

	/**
	 * Returns start date of this round.
	 *
	 * @return the start date of this round.
	 */
	Date getStartDate();

	/**
	 * Returns finish date of this round.
	 *
	 * @return the finish date of this round or {@code null} if round is not finished.
	 */
	Date getFinishDate();

	/**
	 * Returns tournament which this round is associated to.
	 *
	 * @return the tournament owner of this round.
	 */
	Tournament getTournament();
}
