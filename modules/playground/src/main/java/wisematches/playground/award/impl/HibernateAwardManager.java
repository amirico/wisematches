package wisematches.playground.award.impl;

import wisematches.personality.Personality;
import wisematches.playground.award.Award;
import wisematches.playground.award.AwardManager;
import wisematches.playground.award.TrackRecord;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateAwardManager implements AwardManager {
	public HibernateAwardManager() {
	}

	@Override
	public Collection<Award> getAvailableAwards() {
		return null;
	}

	@Override
	public TrackRecord getTrackRecord(Personality person) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}
}
