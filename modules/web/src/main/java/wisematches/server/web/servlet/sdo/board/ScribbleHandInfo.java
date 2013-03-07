package wisematches.server.web.servlet.sdo.board;

import wisematches.core.Personality;
import wisematches.playground.scribble.ScribblePlayerHand;
import wisematches.playground.scribble.Tile;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleHandInfo {
	private final Personality person;
	private final ScribblePlayerHand hand;
	private final boolean showTiles;

	public ScribbleHandInfo(Personality person, ScribblePlayerHand hand, boolean showTiles) {
		this.person = person;
		this.hand = hand;
		this.showTiles = showTiles;
	}

	public long getPlayerId() {
		return person.getId();
	}

	public short getPoints() {
		return hand.getPoints();
	}

	public short getOldRating() {
		return hand.getOldRating();
	}

	public short getNewRating() {
		return hand.getNewRating();
	}

	public boolean isWinner() {
		return hand.isWinner();
	}


	public Tile[] getTiles() {
		if (showTiles) {
			return hand.getTiles();
		}
		return null;
	}
}
