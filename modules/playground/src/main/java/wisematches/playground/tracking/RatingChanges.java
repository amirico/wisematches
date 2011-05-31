package wisematches.playground.tracking;

import wisematches.personality.Personality;
import wisematches.playground.GamePlayerHand;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class RatingChanges {
	private final Map<Long, RatingChange> changeMap = new HashMap<Long, RatingChange>();

	public RatingChanges(Map<Long, RatingChange> changeMap) {
		this.changeMap.putAll(changeMap);
	}

	public RatingChange getRatingChange(GamePlayerHand hand) {
		return changeMap.get(hand.getPlayerId());
	}

	public RatingChange getRatingChange(Personality personality) {
		return changeMap.get(personality.getId());
	}
}