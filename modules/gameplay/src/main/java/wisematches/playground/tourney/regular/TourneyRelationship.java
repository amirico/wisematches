package wisematches.playground.tourney.regular;

import wisematches.playground.GameRelationship;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TourneyRelationship extends GameRelationship {
	public static final int CODE = 1;

	public TourneyRelationship(int tourney) {
		super(CODE, tourney);
	}
}
