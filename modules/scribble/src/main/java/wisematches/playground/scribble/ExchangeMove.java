package wisematches.playground.scribble;

import wisematches.core.Personality;
import wisematches.playground.GameMove;

import java.util.Date;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class ExchangeMove extends GameMove {
	private final int[] tilesIds;

	protected ExchangeMove(Personality player, int points, int moveNumber, Date moveTime, int[] tilesIds) {
		super(player, points, moveNumber, moveTime);
		this.tilesIds = tilesIds;
	}

	//	public ExchangeTilesMove(long gamePlayer, int[] tilesIds) {
//		super(gamePlayer);
//		this.tilesIds = tilesIds.clone();
//	}

	public int[] getTilesIds() {
		return tilesIds.clone();
	}
}
