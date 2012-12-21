package wisematches.playground.tourney.regular.impl;

import wisematches.playground.tourney.regular.TourneyDivision;
import wisematches.playground.tourney.regular.TourneyGroup;
import wisematches.playground.tourney.regular.TourneyRound;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneyReferee {
	List<HibernateTourneyWinner> getWinnersList(TourneyGroup group, TourneyRound round, TourneyDivision division);
}
