package wisematches.playground.tourney.regular.impl;

import wisematches.personality.Language;
import wisematches.playground.tourney.TourneyPlace;
import wisematches.playground.tourney.regular.TourneyAward;
import wisematches.playground.tourney.regular.TourneySection;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TourneyAwardImpl implements TourneyAward {
	private final int tourney;
	private final Date awardedDate;
	private final Language language;
	private final TourneyPlace medal;
	private final TourneySection section;

	public TourneyAwardImpl(int tourney, Date awardedDate, Language language, TourneySection section, TourneyPlace medal) {
		this.section = section;
		this.awardedDate = awardedDate;
		this.medal = medal;
		this.language = language;
		this.tourney = tourney;
	}

	@Override
	public int getTourney() {
		return tourney;
	}

	public Date getAwardedDate() {
		return awardedDate;
	}

	@Override
	public Language getLanguage() {
		return language;
	}

	@Override
	public TourneySection getSection() {
		return section;
	}

	@Override
	public TourneyPlace getMedal() {
		return medal;
	}


	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("TourneyAwardImpl");
		sb.append("{tourney=").append(tourney);
		sb.append(", awardedDate=").append(awardedDate);
		sb.append(", language=").append(language);
		sb.append(", medal=").append(medal);
		sb.append(", section=").append(section);
		sb.append('}');
		return sb.toString();
	}
}
