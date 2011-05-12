package wisematches.server.playground.scribble.statistic;

import wisematches.server.personality.Personality;
import wisematches.server.standing.statistic.impl.PlayerStatisticFactory;
import wisematches.server.standing.statistic.impl.statistician.DefaultGamesStatistician;
import wisematches.server.standing.statistic.impl.statistician.DefaultRatingsStatistician;
import wisematches.server.standing.statistic.statistician.GamesStatistician;
import wisematches.server.standing.statistic.statistician.MovesStatistician;
import wisematches.server.standing.statistic.statistician.RatingsStatistician;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleStatisticFactory implements PlayerStatisticFactory<ScribblePlayerStatistic> {
	private final GamesStatistician gamesStatistician = new DefaultGamesStatistician();
	private final MovesStatistician movesStatistician = new ScribbleMovesStatistician();
	private final RatingsStatistician ratingsStatistician = new DefaultRatingsStatistician();

	public ScribbleStatisticFactory() {
	}

	@Override
	public Class<ScribblePlayerStatistic> getStatisticType() {
		return ScribblePlayerStatistic.class;
	}

	@Override
	public ScribblePlayerStatistic createPlayerStatistic(Personality personality) {
		return new ScribblePlayerStatistic(personality);
	}

	@Override
	public GamesStatistician getGamesStatistician() {
		return gamesStatistician;
	}

	@Override
	public MovesStatistician getMovesStatistician() {
		return movesStatistician;
	}

	@Override
	public RatingsStatistician getRatingsStatistician() {
		return ratingsStatistician;
	}
}
