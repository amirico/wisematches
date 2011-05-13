package wisematches.tracking.stats.statistician;

import wisematches.personality.Personality;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerStatistician<T extends PlayerStatisticEditor> {
	Class<T> getStatisticType();

	T createPlayerStatistic(Personality person);


	<S extends GameSettings, P extends GamePlayerHand> void updateStatistic(GameBoard<S, P> board, T editor);

	<S extends GameSettings, P extends GamePlayerHand> void updateStatistic(GameBoard<S, P> board, GameMove move, T editor);

	<S extends GameSettings, P extends GamePlayerHand> void updateStatistic(GameBoard<S, P> board, GameResolution resolution, Collection<P> wonPlayers, T editor);
}
