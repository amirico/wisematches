package wisematches.playground.propose.criterion;

import wisematches.playground.propose.Criterion;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class PlayerRestrictions {

	private PlayerRestrictions() {
	}

	public static Criterion rating(final String code, final short rating, final ComparableOperator operator) {
		return operator.createCriterion(code, rating, PlayerProperty.RATING);
	}

	public static Criterion ratingA(final String code, final short rating, final ComparableOperator operator) {
		return operator.createCriterion(code, rating, PlayerProperty.RATING_AVERAGE);
	}

	public static Criterion ratingL(final String code, final short rating, final ComparableOperator operator) {
		return operator.createCriterion(code, rating, PlayerProperty.RATING_MIN);
	}

	public static Criterion ratingH(final String code, final short rating, final ComparableOperator operator) {
		return operator.createCriterion(code, rating, PlayerProperty.RATING_MAX);
	}

	public static Criterion completed(final String code, final int games, final ComparableOperator operator) {
		return operator.createCriterion(code, games, PlayerProperty.COMPLETED_GAMES);
	}

	public static Criterion timeouts(final String code, final int timeouts, final ComparableOperator operator) {
		return operator.createCriterion(code, timeouts, PlayerProperty.TIMEOUTS);
	}
}
