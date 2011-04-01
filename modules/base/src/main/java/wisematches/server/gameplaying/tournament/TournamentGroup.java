/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.gameplaying.tournament;

import wisematches.server.personality.account.Language;

/**
 * {@code TournamentGroup} is a very small part of tournament and contains limited (3 or 4) number of players.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface TournamentGroup {
	/**
	 * Returns language of this group.
	 *
	 * @return the language of the group.
	 */
	Language getLanguage();

	/**
	 * Returns player ids for this group.
	 *
	 * @return the player ids for the group.
	 */
	long[] getPlayersInGroup();

	/**
	 * Returns round of this group.
	 *
	 * @return the round of the group.
	 */
	TournamentRound getTournamentRound();

	/**
	 * Returns matrix of played game states. This is two-dimension array like:
	 * <pre>
	 *         | Player0 | Player1 |   ...   | Player N
	 * Player0 |    x    |         |         |
	 * Player1 |         |    x    |         |
	 * ...     |         |         |    x    |
	 * PlayerN |         |         |         |    x
	 * </pre>
	 *
	 * @return the matrix of played games in this group.
	 */
	TournamentGroupState[][] getTournamentGroupMatrix();
}