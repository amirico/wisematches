package wisematches.playground.history;

import wisematches.personality.Language;
import wisematches.playground.GameResolution;

import java.util.Arrays;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameHistory {
	long getBoardId();

	Date getStartedDate();

	Date getFinishedDate();

	boolean isRated();

	Language getLanguage();

	GameResolution getResolution();

	int getOldRating();

	int getNewRating();

	int getMovesCount();

	long[] getPlayers();
}
