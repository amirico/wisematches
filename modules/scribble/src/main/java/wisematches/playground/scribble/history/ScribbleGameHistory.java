package wisematches.playground.scribble.history;

import wisematches.playground.history.GameHistory;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@NamedNativeQueries({
		@NamedNativeQuery(
				name = "scribble.history",
				query =
						"select board.*, max(if(r.playerIndex=0, r.playerId, 0)) player0, max(if(r.playerIndex=1, r.playerId, 0)) player1, max(if(r.playerIndex=2, r.playerId, 0)) player2, max(if(r.playerIndex=3, r.playerId, 0)) player3 " +
								"from (select board.boardId, board.rated, board.language, board.playersCount, board.resolution, board.movesCount, board.startedDate, board.finishedDate, rating.newRating rating, rating.newRating-rating.oldRating ratingChange from scribble_board as board, scribble_player as hand, rating_history as rating where not board.resolution is null and board.boardId=hand.boardId and board.boardId=rating.boardId and rating.playerId=hand.playerId and hand.playerId=?) as board, scribble_player as r " +
								"where r.boardId=board.boardId group by board.boardId order by board.boardId",
				resultSetMapping = "scribble.history")
})
@SqlResultSetMapping(name = "scribble.history",
		entities = @EntityResult(entityClass = ScribbleGameHistory.class))
public class ScribbleGameHistory implements GameHistory {
	@Id
	private long boardId;
	private Date startedDate;
	private Date finishedDate;
	private boolean rated;
	private String language;
	private int rating;
	private int ratingChange;
	private int movesCount;
	private long player0;
	private long player1;
	private long player2;
	private long player3;

	public ScribbleGameHistory() {
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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getRatingChange() {
		return ratingChange;
	}

	public void setRatingChange(int ratingChange) {
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

	@Override
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
		sb.append(", language='").append(language).append('\'');
		sb.append(", rating=").append(rating);
		sb.append(", ratingChange=").append(ratingChange);
		sb.append(", movesCount=").append(movesCount);
		sb.append(", player0=").append(player0);
		sb.append(", player1=").append(player1);
		sb.append(", player2=").append(player2);
		sb.append(", player3=").append(player3);
		sb.append('}');
		return sb.toString();
	}
}
