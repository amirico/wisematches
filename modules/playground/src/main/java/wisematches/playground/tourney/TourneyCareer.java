package wisematches.playground.tourney;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneyCareer {
	/**
	 * Returns number of medals.
	 *
	 * @param medal the medal type
	 * @return number of awarded medals.
	 */
	int getMedalsCount(TourneyMedal medal);
}
