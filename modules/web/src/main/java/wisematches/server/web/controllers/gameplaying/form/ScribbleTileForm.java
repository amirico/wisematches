package wisematches.server.web.controllers.gameplaying.form;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import wisematches.server.gameplaying.scribble.Tile;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScribbleTileForm {
	private int cost;
	private int number;
	private char letter;

	public ScribbleTileForm() {
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public char getLetter() {
		return letter;
	}

	public void setLetter(char letter) {
		this.letter = letter;
	}

	public Tile createTile() {
		return new Tile(number, letter, cost);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("ScribbleTileForm");
		sb.append("{cost=").append(cost);
		sb.append(", number=").append(number);
		sb.append(", letter=").append(letter);
		sb.append('}');
		return sb.toString();
	}
}

