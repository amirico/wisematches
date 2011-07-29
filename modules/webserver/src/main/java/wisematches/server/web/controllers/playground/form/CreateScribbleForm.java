package wisematches.server.web.controllers.playground.form;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class CreateScribbleForm {
	@NotEmpty(message = "game.create.title.err.blank")
	@Length(max = 150, message = "game.create.title.err.max")
	private String title;
	@NotEmpty(message = "game.create.language.err.blank")
	private String boardLanguage;
	private String opponent1;
	private String opponent2 = "no";
	private String opponent3 = "no";
	private int daysPerMove = 3;
	private int minRating = 0;
	private int maxRating = 0;

	public CreateScribbleForm() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBoardLanguage() {
		return boardLanguage;
	}

	public void setBoardLanguage(String boardLanguage) {
		this.boardLanguage = boardLanguage;
	}

	public int getDaysPerMove() {
		return daysPerMove;
	}

	public void setDaysPerMove(int daysPerMove) {
		this.daysPerMove = daysPerMove;
	}

	public int getMinRating() {
		return minRating;
	}

	public void setMinRating(int minRating) {
		this.minRating = minRating;
	}

	public int getMaxRating() {
		return maxRating;
	}

	public void setMaxRating(int maxRating) {
		this.maxRating = maxRating;
	}

	public String getOpponent1() {
		return opponent1;
	}

	public void setOpponent1(String opponent1) {
		this.opponent1 = opponent1;
	}

	public String getOpponent2() {
		return opponent2;
	}

	public void setOpponent2(String opponent2) {
		this.opponent2 = opponent2;
	}

	public String getOpponent3() {
		return opponent3;
	}

	public void setOpponent3(String opponent3) {
		this.opponent3 = opponent3;
	}

	public String[] getOpponents() {
		return new String[]{opponent1, opponent2, opponent3};
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("CreateScribbleForm");
		sb.append("{title='").append(title).append('\'');
		sb.append(", boardLanguage='").append(boardLanguage).append('\'');
		sb.append(", opponent1='").append(opponent1).append('\'');
		sb.append(", opponent2='").append(opponent2).append('\'');
		sb.append(", opponent3='").append(opponent3).append('\'');
		sb.append(", daysPerMove=").append(daysPerMove);
		sb.append(", minRating=").append(minRating);
		sb.append(", maxRating=").append(maxRating);
		sb.append('}');
		return sb.toString();
	}
}
