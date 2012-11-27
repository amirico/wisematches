package wisematches.playground.tourney.regular;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum WinnerPlace {
	FIRST,
	SECOND,
	THIRD;

	private WinnerPlace() {
	}

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
}
