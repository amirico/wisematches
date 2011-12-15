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
	GRANDMASTER(Integer.MAX_VALUE),
	EXPERT(2000),
	ADVANCED(1700),
	INTERMEDIATE(1500),
	CASUAL(1300);

	private final int topRating;

	TournamentSection(int topRating) {
		this.topRating = topRating;
	}

	/**
	 * Returns max allowed rating for this group.
	 *
	 * @return the max allowed rating for this group.
	 */
	public int getTopRating() {
		return topRating;
	}
}
