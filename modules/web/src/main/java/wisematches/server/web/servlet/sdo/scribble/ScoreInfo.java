package wisematches.server.web.servlet.sdo.scribble;

import wisematches.playground.GamePlayerHand;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScoreInfo {
	private final GamePlayerHand playerHand;

	public ScoreInfo(GamePlayerHand playerHand) {
		this.playerHand = playerHand;
	}

	public short getPoints() {
		return playerHand.getPoints();
	}

	public short getNewRating() {
		return playerHand.getNewRating();
	}

	public short getOldRating() {
		return playerHand.getOldRating();
	}

	public boolean isWinner() {
		return playerHand.isWinner();
	}
}
