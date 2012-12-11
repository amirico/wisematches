package wisematches.playground.tourney.regular;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum WinnerPlace {
	FIRST() {
		@Override
		public WinnerPlace nextPlace() {
			return SECOND;
		}
	},
	SECOND() {
		@Override
		public WinnerPlace nextPlace() {
			return THIRD;
		}
	},
	THIRD() {
		@Override
		public WinnerPlace nextPlace() {
			return null;
		}
	};

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

	public int getPlace() {
		return ordinal() + 1;
	}

	public abstract WinnerPlace nextPlace();
}
