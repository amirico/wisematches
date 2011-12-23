/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.playground.tournament;

/**
 * Tournament is divided into groups according to player's rating and has a low rating level. If player
 * has rating highye that low level he/she can't play in this group.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public enum TournamentSection {
	//	!!!WARNING: ORDER IS VERY IMPORTANT. DATABASE MUST BE UPDATED IF CHANGED!!!
	CASUAL(1300),
	INTERMEDIATE(1500),
	ADVANCED(1700),
	EXPERT(2000),
	GRANDMASTER(Integer.MAX_VALUE);

	private final int ratingThreshold;

	TournamentSection(int ratingThreshold) {
		this.ratingThreshold = ratingThreshold;
	}

	/**
	 * Returns max allowed rating for this group.
	 *
	 * @return the max allowed rating for this group.
	 */
	public int getRatingThreshold() {
		return ratingThreshold;
	}
}
