package wisematches.playground.criteria;

import wisematches.personality.player.Player;
import wisematches.playground.tracking.Statistics;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class PlayerRestrictions {

	private PlayerRestrictions() {
	}

	public static PlayerCriterion rating(final String code, final short rating, final ComparableOperator operator) {
		return operator.createCriterion(code, rating, PlayerProperty.RATING);
	}

	public static PlayerCriterion ratingA(final String code, final short rating, final ComparableOperator operator) {
		return operator.createCriterion(code, rating, PlayerProperty.RATING_AVERAGE);
	}

	public static PlayerCriterion ratingL(final String code, final short rating, final ComparableOperator operator) {
		return operator.createCriterion(code, rating, PlayerProperty.RATING_MIN);
	}

	public static PlayerCriterion ratingH(final String code, final short rating, final ComparableOperator operator) {
		return operator.createCriterion(code, rating, PlayerProperty.RATING_MAX);
	}

	public static PlayerCriterion completed(final String code, final int games, final ComparableOperator operator) {
		return operator.createCriterion(code, games, PlayerProperty.COMPLETED_GAMES);
	}

	public static PlayerCriterion timeouts(final String code, final int timeouts, final ComparableOperator operator) {
		return operator.createCriterion(code, timeouts, PlayerProperty.TIMEOUTS);
	}

	public static PlayerCriterion or(final PlayerCriterion lpc, final PlayerCriterion rpc, final PlayerCriterion... others) {
		return new PlayerCriterion() {
			@Override
			public CriterionViolation checkViolation(Player player, Statistics statistics) {
				final CriterionViolation lv = lpc.checkViolation(player, statistics);
				if (lv == null) {
					return null;
				}

				final CriterionViolation rv = rpc.checkViolation(player, statistics);
				if (rv == null) {
					return null;
				}

				if (others != null) {
					for (PlayerCriterion criterion : others) {
						final CriterionViolation v = criterion.checkViolation(player, statistics);
						if (v == null) {
							return null;
						}
					}
				}
				return lv;
			}
		};
	}

	public static PlayerCriterion and(final PlayerCriterion lpc, final PlayerCriterion rpc, final PlayerCriterion... others) {
		return new PlayerCriterion() {
			@Override
			public CriterionViolation checkViolation(Player player, Statistics statistics) {
				final CriterionViolation lv = lpc.checkViolation(player, statistics);
				if (lv != null) {
					return lv;
				}

				final CriterionViolation rv = rpc.checkViolation(player, statistics);
				if (rv != null) {
					return lv;
				}

				if (others != null) {
					for (PlayerCriterion criterion : others) {
						final CriterionViolation v = criterion.checkViolation(player, statistics);
						if (v != null) {
							return v;
						}
					}
				}
				return null;
			}
		};
	}
}
