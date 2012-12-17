package wisematches.playground.tourney;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum TourneyMedal {
	GOLD,
	SILVER,
	BRONZE;

	public Collection<TourneyConqueror> filter(Collection<TourneyConqueror> winners) {
		if (winners == null) {
			return winners;
		}
		Collection<TourneyConqueror> res = new ArrayList<>(winners.size());
		for (TourneyConqueror winner : winners) {
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
