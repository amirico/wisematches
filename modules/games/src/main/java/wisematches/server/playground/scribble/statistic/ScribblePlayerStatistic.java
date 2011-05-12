package wisematches.server.playground.scribble.statistic;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import wisematches.server.personality.Personality;
import wisematches.server.standing.statistic.statistician.GamesStatisticEditor;
import wisematches.server.standing.statistic.statistician.PlayerStatisticEditor;
import wisematches.server.standing.statistic.statistician.RatingsStatisticEditor;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Entity
@Table(name = "rating_statistic")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ScribblePlayerStatistic extends PlayerStatisticEditor {
	@Deprecated
	ScribblePlayerStatistic() {
	}

	public ScribblePlayerStatistic(Personality personality) {
		super(personality, new GamesStatisticEditor(), new ScribbleMovesStatisticEditor(), new RatingsStatisticEditor());
	}

	@Override
	public ScribbleMovesStatistic getMovesStatistic() {
		return (ScribbleMovesStatistic) super.getMovesStatistic();
	}

	@Override
	public ScribbleMovesStatisticEditor getMovesStatisticEditor() {
		return (ScribbleMovesStatisticEditor) super.getMovesStatisticEditor();
	}
}