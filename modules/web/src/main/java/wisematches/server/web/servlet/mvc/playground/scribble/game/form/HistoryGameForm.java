package wisematches.server.web.servlet.mvc.playground.scribble.game.form;

import wisematches.playground.GamePlayerHand;
import wisematches.server.web.servlet.sdo.person.PersonalityData;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HistoryGameForm {
	private long boardId;
	private String title;
	private String language;
	private int movesCount;
	private String resolution;
	private String startedDate;
	private String finishedDate;
	private GamePlayerHand ratingChange;
	private PersonalityData[] opponents;

	public HistoryGameForm() {
	}

	public long getBoardId() {
		return boardId;
	}

	public String getTitle() {
		return title;
	}

	public String getLanguage() {
		return language;
	}

	public int getMovesCount() {
		return movesCount;
	}

	public String getResolution() {
		return resolution;
	}

	public String getStartedDate() {
		return startedDate;
	}

	public String getFinishedDate() {
		return finishedDate;
	}

	public GamePlayerHand getRatingChange() {
		return ratingChange;
	}

	public PersonalityData[] getOpponents() {
		return opponents;
	}

	public void setBoardId(long boardId) {
		this.boardId = boardId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setMovesCount(int movesCount) {
		this.movesCount = movesCount;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public void setStartedDate(String startedDate) {
		this.startedDate = startedDate;
	}

	public void setFinishedDate(String finishedDate) {
		this.finishedDate = finishedDate;
	}

	public void setRatingChange(GamePlayerHand ratingChange) {
		this.ratingChange = ratingChange;
	}

	public void setOpponents(PersonalityData[] opponents) {
		this.opponents = opponents;
	}
}
