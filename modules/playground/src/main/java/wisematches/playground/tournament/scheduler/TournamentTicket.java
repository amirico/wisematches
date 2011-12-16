/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */
package wisematches.playground.tournament.scheduler;

import wisematches.personality.Language;
import wisematches.playground.tournament.TournamentSection;

/**
 * This class contains information about subscription for player.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface TournamentTicket {
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
