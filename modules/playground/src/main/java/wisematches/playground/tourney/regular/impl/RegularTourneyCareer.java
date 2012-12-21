package wisematches.playground.tourney.regular.impl;

import wisematches.playground.tourney.TourneyCareer;
import wisematches.playground.tourney.TourneyPlace;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RegularTourneyCareer implements TourneyCareer {
	private int goldMedals;
	private int silverMedals;
	private int bronzeMedals;

	public RegularTourneyCareer(int goldMedals, int silverMedals, int bronzeMedals) {
		this.goldMedals = goldMedals;
		this.silverMedals = silverMedals;
		this.bronzeMedals = bronzeMedals;
	}

	@Override
	public int getMedalsCount(TourneyPlace medal) {
		switch (medal) {
			case FIRST:
				return goldMedals;
			case SECOND:
				return silverMedals;
			case THIRD:
				return bronzeMedals;
		}
		throw new IllegalArgumentException("Medal type is unknown: " + medal);
	}
}
