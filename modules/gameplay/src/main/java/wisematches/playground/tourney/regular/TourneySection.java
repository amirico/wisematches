package wisematches.playground.tourney.regular;

/**
 * There are 5 tourney sections: Grandmaster, Expert, Advanced, Intermediate and Casual.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum TourneySection {
	//	!!!WARNING: ORDER IS VERY IMPORTANT. DATABASE MUST BE UPDATED IF CHANGED!!!
	CASUAL(1300),
	INTERMEDIATE(1500),
	ADVANCED(1700),
	EXPERT(2000),
	GRANDMASTER(Integer.MAX_VALUE);

	private final int topRating;

	TourneySection(int topRating) {
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

	/**
	 * Returns {@code true} if this section allows join players with any ratings.
	 *
	 * @return {@code true} if any rating is allowed for this section.
	 */
	public boolean isAnyRating() {
		return topRating == Integer.MAX_VALUE;
	}

	/**
	 * Indicates is player with specified rating can be take place in that section.
	 * <p/>
	 * Player can play in a section if it's rating less that top rating of the section.
	 *
	 * @param rating the rating to be checked
	 * @return {@code true} if player can be registered to the section; {@code false} - otherwise.
	 */
	public boolean isRatingAllowed(short rating) {
		return rating < topRating;
	}

	/**
	 * Returns next higher section.
	 *
	 * @return next higher section.
	 */
	public TourneySection getHigherSection() {
		final TourneySection[] values = values();
		final int ordinal = ordinal();
		if (ordinal + 1 >= values.length) {
			return null;
		}
		return values[ordinal + 1];
	}
}
