package wisematches.playground.tourney.regular.impl;

import wisematches.playground.tourney.TourneyCareer;
import wisematches.playground.tourney.TourneyMedal;

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
	public int getMedalsCount(TourneyMedal medal) {
		switch (medal) {
			case GOLD:
				return goldMedals;
			case SILVER:
				return silverMedals;
			case BRONZE:
				return bronzeMedals;
		}
		throw new IllegalArgumentException("Medal type is unknown: " + medal);
	}
}
