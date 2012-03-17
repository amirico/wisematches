package wisematches.playground.criteria;

import wisematches.personality.player.Player;
import wisematches.playground.tracking.Statistics;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@SuppressWarnings("unchecked")
public enum PropertyOperator {
	EQ() {
		@Override
		public <T extends Comparable> boolean test(T a, T b) {
			return a.compareTo(b) == 0;
		}
	},
	LT() {
		@Override
		public <T extends Comparable> boolean test(T a, T b) {
			return a.compareTo(b) < 0;
		}
	},
	LE() {
		@Override
		public <T extends Comparable> boolean test(T a, T b) {
			return a.compareTo(b) <= 0;
		}
	},
	GT() {
		@Override
		public <T extends Comparable> boolean test(T a, T b) {
			return a.compareTo(b) > 0;
		}
	},
	GE() {
		@Override
		public <T extends Comparable> boolean test(T a, T b) {
			return a.compareTo(b) >= 0;
		}
	},
	LIKE() {
		@Override
		public <T extends Comparable> boolean test(T a, T b) {
			return ((String) a).matches((String) b);
		}
	};

	public abstract <T extends Comparable> boolean test(T a, T b);

	PlayerCriterion createCriterion(final String code, final Comparable value, final PlayerProperty property) {
		return new PlayerCriterion() {
			@Override
			public CriterionViolation checkViolation(Player player, Statistics statistics) {
				final Comparable comparable = property.getProperty(player, statistics);
				if (!test(comparable, value)) {
					return new CriterionViolation(code, value, comparable, this);
				}
				return null;
			}
		};
	}
}
