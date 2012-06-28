package wisematches.playground.tournament;

import wisematches.personality.Language;

import java.util.Date;

/**
 * The tournament group is last tournament entity that describes players and games for one group.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentGroup extends TournamentEntity {
	/**
	 * Returns number of the group.
	 *
	 * @return the number of the group.
	 */
	int getGroup();

	/**
	 * Returns round fot the group.
	 *
	 * @return the round fot the group.
	 */
	int getRound();

	/**
	 * Returns tournament for the group.
	 *
	 * @return the tournament for the group.
	 */
	int getTournament();

	Language getLanguage();

	TournamentSection getSection();


	Date getStartedDate();

	Date getFinishedDate();


	long[] getGames();

	long[] getPlayers();

	short[] getPoints();


	boolean isFinished();
}