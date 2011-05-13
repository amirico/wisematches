package wisematches.tracking.impl;

import wisematches.personality.Personality;
import wisematches.server.standing.statistic.statistician.GamesStatisticEditor;
import wisematches.server.standing.statistic.statistician.PlayerStatisticEditor;
import wisematches.server.standing.statistic.statistician.RatingsStatisticEditor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "rating_statistic")
public class MockPlayerStatistic extends PlayerStatisticEditor {
	@Deprecated
	protected MockPlayerStatistic() {
	}

	public MockPlayerStatistic(Personality personality) {
		super(personality, new GamesStatisticEditor(), new MockMovesStatisticEditor(), new RatingsStatisticEditor());
	}

	@Override
	public MockMovesStatistic getMovesStatistic() {
		return (MockMovesStatistic) super.getMovesStatistic();
	}

	@Override
	@Embedded
	public MockMovesStatisticEditor getMovesStatisticEditor() {
		return (MockMovesStatisticEditor) super.getMovesStatisticEditor();
	}
}
