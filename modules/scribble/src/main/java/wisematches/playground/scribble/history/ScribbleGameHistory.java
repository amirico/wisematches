package wisematches.playground.scribble.history;

import wisematches.personality.Language;
import wisematches.playground.GameResolution;
import wisematches.playground.history.GameHistory;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
public class ScribbleGameHistory implements GameHistory {
	@Id
	private long boardId;
	private Date startedDate;
	private Date finishedDate;
	private boolean rated;
	private Language language;
	private GameResolution resolution;
	private int newRating;
	private int oldRating;
	private int movesCount;
	private long player0;
	private long player1;
	private long player2;
	private long player3;

	public ScribbleGameHistory() {
	}

	@Override
	public long getBoardId() {
		return boardId;
	}

	public void setBoardId(long boardId) {
		this.boardId = boardId;
	}

	@Override
	public Date getStartedDate() {
		return startedDate;
	}

	public void setStartedDate(Date startedDate) {
		this.startedDate = startedDate;
	}

	@Override
	public Date getFinishedDate() {
		return finishedDate;
	}

	public void setFinishedDate(Date finishedDate) {
		this.finishedDate = finishedDate;
	}

	@Override
	public boolean isRated() {
		return rated;
	}

	public void setRated(boolean rated) {
		this.rated = rated;
	}

	@Override
	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public void setResolution(GameResolution resolution) {
		this.resolution = resolution;
	}

	@Override
	public GameResolution getResolution() {
		return resolution;
	}

	@Override
	public int getOldRating() {
		return newRating;
	}

	public void setNewRating(int newRating) {
		this.newRating = newRating;
	}

	@Override
	public int getNewRating() {
		return oldRating;
	}

	public void setOldRating(int oldRating) {
		this.oldRating = oldRating;
	}

	@Override
	public int getMovesCount() {
		return movesCount;
	}

	public void setMovesCount(int movesCount) {
		this.movesCount = movesCount;
	}

	public long getPlayer0() {
		return player0;
	}

	public void setPlayer0(long player0) {
		this.player0 = player0;
	}

	public long getPlayer1() {
		return player1;
	}

	public void setPlayer1(long player1) {
		this.player1 = player1;
	}

	public long getPlayer2() {
		return player2;
	}

	public void setPlayer2(long player2) {
		this.player2 = player2;
	}

	public long getPlayer3() {
		return player3;
	}

	public void setPlayer3(long player3) {
		this.player3 = player3;
	}

	@Override
	@Transient
	public long[] getPlayers() {
		return new long[]{player0, player1, player2, player3};
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("ScribbleGameHistory");
		sb.append("{boardId=").append(boardId);
		sb.append(", startedDate=").append(startedDate);
		sb.append(", finishedDate=").append(finishedDate);
		sb.append(", rated=").append(rated);
		sb.append(", language=").append(language);
		sb.append(", resolution=").append(resolution);
		sb.append(", newRating=").append(newRating);
		sb.append(", oldRating=").append(oldRating);
		sb.append(", movesCount=").append(movesCount);
		sb.append(", player0=").append(player0);
		sb.append(", player1=").append(player1);
		sb.append(", player2=").append(player2);
		sb.append(", player3=").append(player3);
		sb.append('}');
		return sb.toString();
	}
}
