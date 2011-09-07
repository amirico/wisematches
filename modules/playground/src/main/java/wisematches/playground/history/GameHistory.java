package wisematches.playground.history;

import java.util.Arrays;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameHistory {
	public long getBoardId();

	public Date getStartedDate();

	public Date getFinishedDate();

	public boolean isRated();

	public String getLanguage();

	public int getRating();

	public int getRatingChange();

	public int getMovesCount();

	public long[] getPlayers();
}
