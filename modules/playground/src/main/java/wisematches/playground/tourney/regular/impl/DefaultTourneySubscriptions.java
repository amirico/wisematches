package wisematches.playground.tourney.regular.impl;

import wisematches.personality.Language;
import wisematches.playground.tourney.regular.TourneySection;
import wisematches.playground.tourney.regular.TourneySubscriptions;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultTourneySubscriptions implements TourneySubscriptions {
	private final int tourney;
	private final int[][] values = new int[Language.values().length][TourneySection.values().length];

	public DefaultTourneySubscriptions(int tourney) {
		this.tourney = tourney;
	}

	@Override
	public int getTourney() {
		return tourney;
	}

	@Override
	public int getPlayers(Language language) {
		int res = 0;
		for (int count : values[language.ordinal()]) {
			res += count;
		}
		return res;
	}

	@Override
	public int getPlayers(Language language, TourneySection section) {
		return values[language.ordinal()][section.ordinal()];
	}

	void setPlayers(Language language, TourneySection section, int count) {
		values[language.ordinal()][section.ordinal()] = count;
	}
}
