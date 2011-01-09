/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.core.tournament;

import java.util.Date;

/**
 * This is container for tournament information. Each tournament has some parameters, like
 * status, number, start and finish date, and also collection of rounds in this tournament.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface Tournament {
    /**
     * Returns number of this tournament.
     *
     * @return the number of the tournament.
     */
    int getNumber();

    /**
     * Returns start date of the tournament. If date in future - it's announced tournament.
     *
     * @return the start date of the tournament
     */
    Date getStartDate();

    /**
     * Returns date when tournament was finished.
     *
     * @return the date when tournament was finished or {@code null} if tournament is not finished.
     */
    Date getFinishDate();

    /**
     * Returns status of this tournament.
     *
     * @return the status of this tournament.
     */
    TournamentStatus getTournamentStatus();
}