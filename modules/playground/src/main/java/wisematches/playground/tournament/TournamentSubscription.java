/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.playground.tournament;

import wisematches.personality.Language;

/**
 * This class contains information about subscription for player.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface TournamentSubscription {
	/**
	 * Returns tournament language.
	 *
	 * @return the tournament language.
	 */
	Language getLanguage();

	/**
	 * Returns subscribed section.
	 *
	 * @return the subscribed section.
	 */
	TournamentSection getSection();
}
