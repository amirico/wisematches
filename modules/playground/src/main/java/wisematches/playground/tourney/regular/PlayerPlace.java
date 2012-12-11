package wisematches.playground.tourney.regular;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum PlayerPlace {
	FIRST() {
		@Override
		public PlayerPlace nextPlace() {
			return SECOND;
		}
	},
	SECOND() {
		@Override
		public PlayerPlace nextPlace() {
			return THIRD;
		}
	},
	THIRD() {
		@Override
		public PlayerPlace nextPlace() {
			return null;
		}
	};

	private PlayerPlace() {
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

	public abstract PlayerPlace nextPlace();
}
