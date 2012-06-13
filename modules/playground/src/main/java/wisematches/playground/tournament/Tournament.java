package wisematches.playground.tournament;

import java.util.Date;

/**
 * Base interface for tournament representation. Each tournament has unique number,
 * start date and finish date.
 * <p/>
 * Each tournament contains set of rounds and each round has set of groups.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface Tournament extends TournamentEntity {
    /**
     * Returns number of the tournament.
     *
     * @return the number of the tournament.
     */
    int getNumber();

    /**
     * Returns start date of the tournament. If the date in the future when the tournament
     * was announced but not started yet.
     *
     * @return the start date
     */
    Date getStartedDate();

    /**
     * Returns finish date of the tournament. If date is null when tournament is not finished or not started yet.
     *
     * @return the finish date or {@code null} if the tournament isn't finished.
     */
    Date getFinishedDate();

    /**
     * The scheduled date for this tournament. Has meaning only for not initiated tournaments.
     *
     * @return the tournament scheduled date.
     */
    Date getScheduledDate();

    /**
     * Returns tournament state
     *
     * @return the tournament state
     */
    TournamentState getTournamentState();
}
