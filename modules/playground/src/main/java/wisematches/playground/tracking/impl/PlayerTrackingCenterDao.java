package wisematches.playground.tracking.impl;

import wisematches.core.Personality;
import wisematches.playground.tracking.RatingCurve;
import wisematches.playground.tracking.StatisticsEditor;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerTrackingCenterDao {
	short getRating(Personality person);

	RatingCurve getRatingChangesCurve(Personality player, int resolution, Date startDate, Date endDate);

	<T extends StatisticsEditor> T loadPlayerStatistic(Class<? extends T> clazz, Personality personality);

	void savePlayerStatistic(StatisticsEditor statistic);

	void removePlayerStatistic(StatisticsEditor statistic);
}
