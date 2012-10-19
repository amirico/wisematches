package wisematches.playground.tourney.regular.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class DefaultTourneyProcessor {
	private DefaultTourneyProcessor() {
	}

	static List<long[]> splitByGroups(Collection<Long> players) {
		int elapsedCount = players.size();
		final Iterator<Long> iterator = players.iterator();
		final List<long[]> res = new ArrayList<long[]>();
		while (elapsedCount != 0) {
			if (elapsedCount >= 12) {
				res.add(takeNextPlayers(iterator, 4));
				elapsedCount -= 4;
			} else if (elapsedCount == 4 || elapsedCount == 7 || elapsedCount == 8 || elapsedCount == 10 || elapsedCount == 11) {
				res.add(takeNextPlayers(iterator, 4));
				elapsedCount -= 4;
			} else if (elapsedCount == 3 || elapsedCount == 5 || elapsedCount == 6 || elapsedCount == 9) {
				res.add(takeNextPlayers(iterator, 3));
				elapsedCount -= 3;
			} else if (elapsedCount == 2) {
				res.add(takeNextPlayers(iterator, 2));
				elapsedCount -= 2;
			}
		}

		if (iterator.hasNext()) {
			throw new IllegalStateException("After processing we still have players in iterator");
		}
		return res;
	}

	private static long[] takeNextPlayers(Iterator<Long> players, int count) {
		final long[] res = new long[count];
		for (int i = 0; i < res.length; i++) {
			res[i] = players.next();
		}
		return res;
	}
}