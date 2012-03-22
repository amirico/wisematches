package wisematches.playground.criteria;

import wisematches.personality.player.Player;
import wisematches.playground.tracking.Statistics;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class PlayerRestrictions {

	private PlayerRestrictions() {
	}

	public static PlayerCriterion id(final long id) {
		return ComparableOperator.EQ.createCriterion("player.id", id, PlayerProperty.ID);
	}

	public static PlayerCriterion rating(final short rating, final ComparableOperator operator) {
		return operator.createCriterion("player.rating", rating, PlayerProperty.RATING);
	}

	public static PlayerCriterion ratingA(final short rating, final ComparableOperator operator) {
		return operator.createCriterion("player.rating.average", rating, PlayerProperty.RATING_AVERAGE);
	}

	public static PlayerCriterion ratingL(final short rating, final ComparableOperator operator) {
		return operator.createCriterion("player.rating.min", rating, PlayerProperty.RATING_MIN);
	}

	public static PlayerCriterion ratingH(final short rating, final ComparableOperator operator) {
		return operator.createCriterion("player.rating.max", rating, PlayerProperty.RATING_MAX);
	}

	public static PlayerCriterion completed(final short rating, final ComparableOperator operator) {
		return operator.createCriterion("games.completed", rating, PlayerProperty.COMPLETED_GAMES);
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
