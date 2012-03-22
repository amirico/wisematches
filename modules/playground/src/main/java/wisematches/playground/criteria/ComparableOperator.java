package wisematches.playground.criteria;

import wisematches.personality.player.Player;
import wisematches.playground.tracking.Statistics;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@SuppressWarnings("unchecked")
public enum ComparableOperator {
	EQ() {
		@Override
		<T extends Comparable> boolean test(T a, T b) {
			return a.compareTo(b) == 0;
		}
	},
	LT() {
		@Override
		<T extends Comparable> boolean test(T a, T b) {
			return a.compareTo(b) < 0;
		}
	},
	LE() {
		@Override
		<T extends Comparable> boolean test(T a, T b) {
			return a.compareTo(b) <= 0;
		}
	},
	GT() {
		@Override
		<T extends Comparable> boolean test(T a, T b) {
			return a.compareTo(b) > 0;
		}
	},
	GE() {
		@Override
		<T extends Comparable> boolean test(T a, T b) {
			return a.compareTo(b) >= 0;
		}
	};

	abstract <T extends Comparable> boolean test(T a, T b);

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
