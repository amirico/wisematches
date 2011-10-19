package wisematches.playground.scribble.search.board;

import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.playground.GameResolution;
import wisematches.playground.search.DesiredEntityBean;
import wisematches.playground.search.SearchAttribute;
import wisematches.playground.search.SearchDistinct;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@SearchDistinct("boardId")
public class ScribbleHistoryEntity implements DesiredEntityBean<Void> {
	@Id
	@SearchAttribute(column = "board.boardId")
	private long boardId;

	@SearchAttribute(column = "board.startedDate")
	private Date startedDate;

	@SearchAttribute(column = "board.finishedDate")
	private Date finishedDate;

	@SearchAttribute(column = "board.rated")
	private boolean rated;

	@SearchAttribute(column = "UPPER(board.language)")
	private Language language;

	@SearchAttribute(column = "board.resolution")
	private GameResolution resolution;

	@SearchAttribute(column = "board.movesCount")
	private int movesCount;

	@SearchAttribute(column = "l.newRating")
	private short newRating;

	@SearchAttribute(column = "l.newRating - l.oldRating")
	private short ratingChange;

	@SearchAttribute(column = "max(if(r.playerIndex=0, r.playerId, 0))", sortable = false)
	private long player0;

	@SearchAttribute(column = "max(if(r.playerIndex=1, r.playerId, 0))", sortable = false)
	private long player1;

	@SearchAttribute(column = "max(if(r.playerIndex=2, r.playerId, 0))", sortable = false)
	private long player2;

	@SearchAttribute(column = "max(if(r.playerIndex=3, r.playerId, 0))", sortable = false)
	private long player3;

	public ScribbleHistoryEntity() {
	}

	public long getBoardId() {
		return boardId;
	}

	public void setBoardId(long boardId) {
		this.boardId = boardId;
	}

	public Date getStartedDate() {
		return startedDate;
	}

	public void setStartedDate(Date startedDate) {
		this.startedDate = startedDate;
	}

	public Date getFinishedDate() {
		return finishedDate;
	}

	public void setFinishedDate(Date finishedDate) {
		this.finishedDate = finishedDate;
	}

	public boolean isRated() {
		return rated;
	}

	public void setRated(boolean rated) {
		this.rated = rated;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public GameResolution getResolution() {
		return resolution;
	}

	public void setResolution(GameResolution resolution) {
		this.resolution = resolution;
	}

	public short getNewRating() {
		return newRating;
	}

	public void setNewRating(short newRating) {
		this.newRating = newRating;
	}

	public short getRatingChange() {
		return ratingChange;
	}

	public void setRatingChange(short ratingChange) {
		this.ratingChange = ratingChange;
	}

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

	public long[] getPlayers(Personality personality) {
		int c = 0;
		if (isDefined(player0, personality)) {
			c++;
		}
		if (isDefined(player1, personality)) {
			c++;
		}
		if (isDefined(player2, personality)) {
			c++;
		}
		if (isDefined(player3, personality)) {
			c++;
		}
		long[] res = new long[c];
		c = 0;
		if (isDefined(player0, personality)) {
			res[c++] = player0;
		}
		if (isDefined(player1, personality)) {
			res[c++] = player1;
		}
		if (isDefined(player2, personality)) {
			res[c++] = player2;
		}
		if (isDefined(player3, personality)) {
			res[c] = player3;
		}
		return res;
	}

	private boolean isDefined(long id, Personality p) {
		return id != 0 && id != p.getId();
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
		sb.append(", movesCount=").append(movesCount);
		sb.append(", newRating=").append(newRating);
		sb.append(", ratingChange=").append(ratingChange);
		sb.append(", player0=").append(player0);
		sb.append(", player1=").append(player1);
		sb.append(", player2=").append(player2);
		sb.append(", player3=").append(player3);
		sb.append('}');
		return sb.toString();
	}
}
