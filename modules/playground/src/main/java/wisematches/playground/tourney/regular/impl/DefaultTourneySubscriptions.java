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
	public int getPlayers() {
		int res = 0;
		for (int[] value : values) {
			for (int i : value) {
				res += i;
			}
		}
		return res;
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

	@Override
	public boolean hasPlayers(Language language, TourneySection section) {
		return getPlayers(language, section) != 0;
	}

	void setPlayers(Language language, TourneySection section, int count) {
		values[language.ordinal()][section.ordinal()] = count;
	}


	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("DefaultTourneySubscriptions");
		sb.append("{tourney=").append(tourney);
		sb.append(",");
		for (Language language : Language.values()) {
			for (TourneySection section : TourneySection.values()) {
				sb.append(", [").append(language.name()).append("][").append(section.name()).append("]=").append(values[language.ordinal()][section.ordinal()]).append("]");
			}
		}
		sb.append('}');
		return sb.toString();
	}
}
