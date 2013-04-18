package wisematches.playground.propose.criterion;

import wisematches.core.Player;
import wisematches.playground.propose.Criterion;
import wisematches.playground.propose.CriterionViolation;
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

	Criterion createCriterion(final String code, final Comparable value, final PlayerProperty property) {
		return new Criterion() {
			@Override
			public String getCode() {
				return code;
			}

			@Override
			public Comparable getExpected() {
				return value;
			}

			@Override
			public CriterionViolation checkViolation(Player player, Statistics statistics) {
				final Comparable comparable = property.getProperty(player, statistics);
				if (!test(comparable, value)) {
					return new CriterionViolation(code, comparable, value);
				}
				return null;
			}
		};
	}
}
