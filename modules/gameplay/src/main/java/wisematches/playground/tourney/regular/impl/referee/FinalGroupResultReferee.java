package wisematches.playground.tourney.regular.impl.referee;

import wisematches.playground.tourney.TourneyPlace;
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

		int medal = 0;
		final TourneyPlace[] medals = TourneyPlace.values();
		final List<HibernateTourneyWinner> winners = new ArrayList<>();
		for (Iterator<Integer> iterator = scores.iterator(); iterator.hasNext() && medal < medals.length; medal++) {
			final int score = iterator.next();
			for (long player : players) {
				if (scoresSet.get(player) == score) { // if player has the same scores - add to winners
					winners.add(new HibernateTourneyWinner(player, medals[medal]));
				}
			}
		}
		return winners;
	}
}
