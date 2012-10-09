package wisematches.playground.scribble;

import wisematches.playground.MakeTurnMove;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class ExchangeTilesMove extends MakeTurnMove {
	private final int[] tilesIds;

	public ExchangeTilesMove(long gamePlayer, int[] tilesIds) {
		super(gamePlayer);
		this.tilesIds = tilesIds.clone();
	}

	public int[] getTilesIds() {
		return tilesIds.clone();
	}
}
