package wisematches.playground.tourney.regular.impl.referee;

import wisematches.playground.tourney.regular.TourneyDivision;
import wisematches.playground.tourney.regular.TourneyGroup;
import wisematches.playground.tourney.regular.TourneyRound;
import wisematches.playground.tourney.regular.WinnerPlace;
import wisematches.playground.tourney.regular.impl.HibernateTourneyWinner;
import wisematches.playground.tourney.regular.impl.TourneyReferee;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FinalGroupResultReferee implements TourneyReferee {
	public FinalGroupResultReferee() {
	}

	@Override
	public List<HibernateTourneyWinner> getWinnersList(TourneyGroup group,
													   TourneyRound round,
													   TourneyDivision division) {
		// remove duplicates
		HashSet<Short> scoresSet = new HashSet<>();
		for (short i : group.getScores()) {
			scoresSet.add(i);
		}

		// sort and revert
		final List<Short> scores = new ArrayList<>(scoresSet);
		Collections.sort(scores);
		Collections.reverse(scores);

		final List<HibernateTourneyWinner> winners = new ArrayList<>();

		WinnerPlace place = WinnerPlace.FIRST;
		for (Iterator<Short> iterator = scores.iterator(); iterator.hasNext() && place != null; place = place.nextPlace()) {
			final short score = iterator.next();
			for (long player : group.getPlayers()) {
				if (group.getScores(player) == score) { // if player has the same scores - add to winners
					winners.add(new HibernateTourneyWinner(player, place));
				}
			}
		}
		return winners;
	}
}
