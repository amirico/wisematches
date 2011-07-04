package wisematches.playground.tracking.impl;

import wisematches.personality.Personality;
import wisematches.playground.tracking.RatingChange;
import wisematches.playground.tracking.RatingChangesCurve;
import wisematches.playground.tracking.StatisticsEditor;

import java.util.Collection;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerTrackingCenterDao {
	short getRating(Personality person);

	void saveRatingChange(RatingChange ratingChange);

	Collection<RatingChange> getRatingChanges(long boardId);

	RatingChangesCurve getRatingChangesCurve(Personality player, int resolution, Date startDate, Date endDate);

	<T extends StatisticsEditor> T loadPlayerStatistic(Class<? extends T> clazz, Personality personality);

	void savePlayerStatistic(StatisticsEditor statistic);

	void removePlayerStatistic(StatisticsEditor statistic);
}
