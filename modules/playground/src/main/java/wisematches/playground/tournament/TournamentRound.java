package wisematches.playground.tournament;

import wisematches.personality.Language;

import java.util.Date;

/**
 * A tournament round belong to a tournament and
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentRound extends TournamentEntity<TournamentSectionId> {
	/**
	 * Returns number of the round.
	 *
	 * @return the number of the round.
	 */
	int getNumber();

	/**
	 * Returns tournament that contains this round.
	 *
	 * @return the tournament that contains this round.
	 */
	int getTournament();

	/**
	 * Returns language for the round.
	 *
	 * @return the language for the round.
	 */
	Language getLanguage();

	/**
	 * Returns section of this round.
	 *
	 * @return the section of the round.
	 */
	TournamentSection getTournamentSection();

	/**
	 * Returns date when round was started.
	 *
	 * @return the date when round was started.
	 */
	Date getStartDate();

	/**
	 * Returns date when round was finished.
	 *
	 * @return the date when round was finished or {@code null} if round is in progress.
	 */
	Date getFinishDate();

	/**
	 * Returns number of total games in the round.
	 *
	 * @return the number of total games in the round.
	 */
	int getTotalGames();

	/**
	 * Returns number of finished games.
	 *
	 * @return the number of finished games.
	 */
	int getFinishedGames();

	/**
	 * Returns {@code true} if round was finished or {@code false} if not.
	 *
	 * @return {@code true} if round was finished; otherwise {@code false}.
	 */
	boolean isFinished();
}
