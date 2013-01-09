package wisematches.playground.award;

import wisematches.personality.Personality;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface AwardManager {
	/**
	 * Returns collection of all awards.
	 *
	 * @return the collection of all awards.
	 */
	Collection<Award> getAvailableAwards();

	/**
	 * Returns track record for specified player.
	 *
	 * @param person the person who's track record must be returned.
	 * @return track record for specified player.
	 */
	TrackRecord getTrackRecord(Personality person);
}