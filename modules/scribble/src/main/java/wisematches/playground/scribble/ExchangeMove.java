package wisematches.playground.scribble;

import wisematches.core.Personality;
import wisematches.playground.GameMove;

import java.util.Date;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class ExchangeMove extends GameMove {
	private final int[] tilesIds;

	protected ExchangeMove(Personality player, int[] tilesIds) {
		super(player);
		this.tilesIds = tilesIds;
	}

	public ExchangeMove(Personality player, int points, Date moveTime, int[] tilesIds) {
		super(player, points, moveTime);
		this.tilesIds = tilesIds;
	}

	public int[] getTileIds() {
		return tilesIds.clone();
	}
}
