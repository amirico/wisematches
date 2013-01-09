package wisematches.playground.award;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TrackRecord {
	boolean hasRibbon(Award award);

	int getMedalsCount(Award award);

	AwardWeight getBadgeType(Award award);


	int getConditionValue(Award award, AwardCondition condition);
}
