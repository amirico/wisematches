package wisematches.server.standing.stats.imp;

import wisematches.server.personality.Personality;
import wisematches.server.playground.board.*;
import wisematches.server.standing.stats.statistician.PlayerStatisticEditor;
import wisematches.server.standing.stats.statistician.PlayerStatistician;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultPlayerStatistician<T extends PlayerStatisticEditor> implements PlayerStatistician<T> {
	public DefaultPlayerStatistician() {
	}

	@Override
	public Class<T> getStatisticType() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public T createPlayerStatistic(Personality person) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public <S extends GameSettings, P extends GamePlayerHand> void updateStatistic(GameBoard<S, P> board, T editor) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public <S extends GameSettings, P extends GamePlayerHand> void updateStatistic(GameBoard<S, P> board, GameMove move, T editor) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public <S extends GameSettings, P extends GamePlayerHand> void updateStatistic(GameBoard<S, P> board, GameResolution resolution, Collection<P> wonPlayers, T editor) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}
}
