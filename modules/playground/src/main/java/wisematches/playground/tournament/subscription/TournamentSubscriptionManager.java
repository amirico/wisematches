/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */
package wisematches.playground.tournament.subscription;

import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.playground.tournament.Tournament;
import wisematches.playground.tournament.TournamentSection;

/**
 * Additional manager for {@code TournamentManager} which manages players subscription for tournaments.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface TournamentSubscriptionManager {
	/**
	 * Returns tournament which is announced at this moment.
	 * <p/>
	 * This method returns the same tournament as a {@code TournamentManager}
	 *
	 * @return the announced tournament or {@code null} if tournament is not announced yet.
	 * @see wisematches.playground.tournament.TournamentManager#getAnnouncedTournament()
	 */
	Tournament getAnnouncedTournament();

	/**
	 * Subscribe specified player to announced player with specified section and language.
	 *
	 * @param player			the player to be subscribed.
	 * @param tournamentSection the subscribing section.
	 * @param language		  the language of the game.
	 * @throws TournamentSubscriptionException
	 *                              if player already subscribed for announced tournament
	 *                              or it's rating is not correct for specified section or if subscription can't be
	 *                              done by internal error
	 * @throws NullPointerException if any parameter is null
	 */
	void subscribe(Personality player, TournamentSection tournamentSection, Language language) throws TournamentSubscriptionException;

	/**
	 * Unsubscribe player from announced tournament. If player is not subscribed nothing will be happened.
	 *
	 * @param player the player to be unsubscribe.
	 * @throws TournamentSubscriptionException
	 *          if subscription can't be done by internal error
	 */
	void unsubscribe(Personality player) throws TournamentSubscriptionException;

	/**
	 * Checks is specified player subscribed for announced tournament or not.
	 *
	 * @param player the player to be checked.
	 * @return {@code true} if player is subscribed; {@code false} - otherwise.
	 */
	boolean isSubscribed(Personality player);

	/**
	 * Returns subscription information for specified player or {@code null} of player is not subscribed. It's
	 * strongly recommended to use {@code isSubscribed(Player)} method to check is player subscribed. In
	 * some implementations it can be optimazed and work faster.
	 *
	 * @param player the player
	 * @return the subscription info or {@code null} if player is not subscribed to announced tournament.
	 * @see #isSubscribed(wisematches.personality.Personality)
	 */
	TournamentSubscription getSubscription(Personality player);
}
