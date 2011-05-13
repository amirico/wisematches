/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.playground.tournament;

import java.util.List;

/**
 * The manager interface for tournaments.
 * <p/>
 * Manager supported following use-cases for players:
 * <ul>
 * <li>Subscribe/unsubscribe players to/from next tournament;
 * <li>Notify players that a tournament has been started;
 * <li>Check is player already subscribed to tournament;
 * <li>Get list of tournaments for players;
 * <li>Get player's position in tournament;
 * <li>Get player's rounds for tournament.
 * </ul>
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface TournamentManager {
	/**
	 * Adds specified listener to this manager. This listener will be notified when next tournamed will be
	 * announce, started, finished and so on.
	 *
	 * @param l the listener to be added.
	 */
	void addTournamentListener(TournamentListener l);

	/**
	 * Removes specified listener from this manager. This listener will not received any notification any more.
	 *
	 * @param l the listener to be removed.
	 */
	void removeTournamentListener(TournamentListener l);


	/**
	 * Returns tournament which is announced at this moment.
	 *
	 * @return the announced tournament or {@code null} if tournament is not announced yet.
	 */
	Tournament getAnnouncedTournament();


	/**
	 * Returns number of tournaments with specified status.
	 * <p/>
	 * If there is no one tournament with specified status empty array returned.
	 *
	 * @param status the status of tournaments which should be returned.
	 * @return the array that contains number of tournaments with specified status or empty array
	 *         if there is no one tournament.
	 */
	int[] getTournaments(TournamentStatus status);

	/**
	 * Returns tournament by it's number.
	 *
	 * @param number the tournament number
	 * @return the tournament by specified number
	 * @throws IllegalArgumentException if number is negative or great or equals number of all tournaments.
	 */
	Tournament getTournament(int number);

	/**
	 * Returns unmodifiable list of all rounds for specified tournament. Rounds in list sorted by round number
	 * where last item - last round.
	 *
	 * @param tournament the tournament which rounds should be returned.
	 * @return the sorted unmodifiable list of all rounds.
	 */
	List<TournamentRound> getTournamentRounds(Tournament tournament);
}