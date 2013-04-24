package wisematches.playground.scribble;

import wisematches.core.Personality;

import java.util.Date;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class ExchangeTiles extends ScribbleMove {
	private final int[] tilesIds;

	protected ExchangeTiles(Personality player, int[] tilesIds) {
		super(player);
		this.tilesIds = tilesIds;
	}

	public ExchangeTiles(Personality player, int points, Date moveTime, int[] tilesIds) {
		super(player, points, moveTime);
		this.tilesIds = tilesIds;
	}

	public int[] getTileIds() {
		return tilesIds.clone();
	}

	@Override
	public ScribbleMoveType getMoveType() {
		return ScribbleMoveType.EXCHANGE;
	}
}
