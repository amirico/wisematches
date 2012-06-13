package wisematches.playground.tournament;

/**
 * There are 5 tournament sections: Grandmaster, Expert, Advanced, Intermediate and Casual.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum TournamentCategory {
    //	!!!WARNING: ORDER IS VERY IMPORTANT. DATABASE MUST BE UPDATED IF CHANGED!!!
    GRANDMASTER(Integer.MAX_VALUE),
    EXPERT(2000),
    ADVANCED(1700),
    INTERMEDIATE(1500),
    CASUAL(1300);

    private final int topRating;

    TournamentCategory(int topRating) {
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
     * Indicates is player with specified rating can be take place in that section.
     * <p/>
     * Player can play in a section if it's rating less that top rating of the section.
     *
     * @param rating the rating to be checked
     * @return {@code true} if player can be subscribed to the section; {@code false} - otherwise.
     */
    public boolean isRatingAllowed(short rating) {
        return rating < topRating;
    }
}
