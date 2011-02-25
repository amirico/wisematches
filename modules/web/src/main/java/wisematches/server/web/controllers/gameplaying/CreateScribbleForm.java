package wisematches.server.web.controllers.gameplaying;

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
	private String language;
	private int daysPerMove;
	private int maxPlayers;
	private int minRating;
	private int maxRating;
	private String opponents;

	public CreateScribbleForm() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getDaysPerMove() {
		return daysPerMove;
	}

	public void setDaysPerMove(int daysPerMove) {
		this.daysPerMove = daysPerMove;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
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

	public String getOpponents() {
		return opponents;
	}

	public void setOpponents(String opponents) {
		this.opponents = opponents;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("CreateScribbleForm");
		sb.append("{title='").append(title).append('\'');
		sb.append(", language='").append(language).append('\'');
		sb.append(", daysPerMove=").append(daysPerMove);
		sb.append(", maxPlayers=").append(maxPlayers);
		sb.append(", minRating=").append(minRating);
		sb.append(", maxRating=").append(maxRating);
		sb.append('}');
		return sb.toString();
	}
}
