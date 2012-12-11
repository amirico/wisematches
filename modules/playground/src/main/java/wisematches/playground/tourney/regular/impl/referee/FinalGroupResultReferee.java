package wisematches.playground.tourney.regular.impl.referee;

import wisematches.playground.tourney.regular.PlayerPlace;
import wisematches.playground.tourney.regular.TourneyDivision;
import wisematches.playground.tourney.regular.TourneyGroup;
import wisematches.playground.tourney.regular.TourneyRound;
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
		final long[] players = group.getPlayers();

		// remove duplicates
		Map<Long, Integer> scoresSet = new HashMap<>();
		for (long player : players) {
			scoresSet.put(player, group.getPlayerScores(player));
		}

		// sort and revert
		final List<Integer> scores = new ArrayList<>(new HashSet<>(scoresSet.values()));
		Collections.sort(scores);
		Collections.reverse(scores);

		final List<HibernateTourneyWinner> winners = new ArrayList<>();

		PlayerPlace place = PlayerPlace.FIRST;
		for (Iterator<Integer> iterator = scores.iterator(); iterator.hasNext() && place != null; place = place.nextPlace()) {
			final int score = iterator.next();
			for (long player : players) {
				if (scoresSet.get(player) == score) { // if player has the same scores - add to winners
					winners.add(new HibernateTourneyWinner(player, place));
				}
			}
		}
		return winners;
	}
}
