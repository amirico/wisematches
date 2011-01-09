/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.core.tournament;

/**
 * This is common exception for any activities related to tournaments.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class TournamentException extends Exception {
    public TournamentException(String message) {
        super(message);
    }

    public TournamentException(String message, Throwable cause) {
        super(message, cause);
    }
}
