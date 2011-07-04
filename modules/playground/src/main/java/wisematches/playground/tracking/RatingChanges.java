package wisematches.playground.tracking;

import wisematches.personality.Personality;
import wisematches.playground.GamePlayerHand;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class RatingChanges implements Iterable<RatingChange> {
	private final Map<Long, RatingChange> changeMap = new HashMap<Long, RatingChange>();

	public RatingChanges(RatingChange... changes) {
		for (RatingChange change : changes) {
			changeMap.put(change.getPlayerId(), change);
		}
	}

	public RatingChanges(Collection<RatingChange> changes) {
		for (RatingChange change : changes) {
			changeMap.put(change.getPlayerId(), change);
		}
	}

	public RatingChange getRatingChange(long playerId) {
		return changeMap.get(playerId);
	}

	public RatingChange getRatingChange(GamePlayerHand hand) {
		return getRatingChange(hand.getPlayerId());
	}

	public RatingChange getRatingChange(Personality personality) {
		return getRatingChange(personality.getId());
	}

	@Override
	public Iterator<RatingChange> iterator() {
		return changeMap.values().iterator();
	}
}