package wisematches.playground.tourney;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum TourneyPlace {
	FIRST,
	SECOND,
	THIRD;

	public Collection<TourneyWinner> filter(Collection<TourneyWinner> winners) {
		if (winners == null) {
			return winners;
		}
		Collection<TourneyWinner> res = new ArrayList<>(winners.size());
		for (TourneyWinner winner : winners) {
			if (winner.getPlace() == this) {
				res.add(winner);
			}
		}
		return res;
	}

	public int getPlace() {
		return ordinal() + 1;
	}
}
