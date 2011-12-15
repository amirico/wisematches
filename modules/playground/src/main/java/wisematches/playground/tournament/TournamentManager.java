/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.playground.tournament;

import wisematches.personality.Language;
import wisematches.personality.Personality;

import java.util.Collection;

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
	 * Adds specified listener to this manager. This listener will be notified when next tournament will be
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
	 * Adds specified listener to this manager. This listener will be notified when a player will subscribe or
	 * unsubscribe from a tournament.
	 *
	 * @param l the listener to be added.
	 */
	void addTournamentSubscriptionListener(TournamentSubscriptionListener l);

	/**
	 * Removes specified listener from this manager. This listener will not received any notification any more.
	 *
	 * @param l the listener to be removed.
	 */
	void removeTournamentSubscriptionListener(TournamentSubscriptionListener l);


	/**
	 * Returns tournament which is announced at this moment.
	 *
	 * @return the announced tournament or {@code null} if tournament is not announced yet.
	 */
	Tournament getAnnouncedTournament();


	/**
	 * Subscribe specified player to announced player with specified section and language.
	 *
	 * @param player			the player to be subscribed.
	 * @param language		  the language of games.
	 * @param tournamentSection the subscribing section.
	 * @throws NullPointerException if any parameter is null
	 */
	void subscribe(Personality player, Language language, TournamentSection section);

	/**
	 * Unsubscribe player from announced tournament. If player is not subscribed nothing will be happened.
	 *
	 * @param player   the player to be unsubscribe.
	 * @param language the language of games
	 */
	void unsubscribe(Personality player, Language language);

	/**
	 * Returns tournament section that the player is subscribed to for specified language. If
	 * player is not subscribed when {@code null} will be returned.
	 *
	 * @param player   the player
	 * @param language the language of game
	 * @return the subscription info or {@code null} if player is not subscribed to announced tournament.
	 */
	TournamentSection getSubscribedSection(Personality player, Language language);

	/**
	 * Returns all subscriptions for selected player (for all languages)
	 *
	 * @param player the player who's subscriptions
	 * @return the collection of all subscriptions for specified player.
	 */
	Collection<TournamentSubscription> getSubscriptions(Personality player);
}