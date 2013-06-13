package wisematches.playground.tourney.regular;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class TourneyEntryIterator<I extends RegularTourneyEntity> {
	private final Map<Tourney, List<I>> itemsMap = new TreeMap<>(TourneyComparator.ASK);

	private TourneyEntryIterator() {
	}

	public Set<Map.Entry<Tourney, List<I>>> getItemsEntry() {
		return itemsMap.entrySet();
	}

	public static TourneyEntryIterator<TourneyGroup> groups(List<TourneyGroup> groups) {
		return iterate(groups);
	}

	public static TourneyEntryIterator<TourneyRound> rounds(List<TourneyRound> rounds) {
		return iterate(rounds);
	}

	public static TourneyEntryIterator<TourneyDivision> divisions(List<TourneyDivision> divisions) {
		return iterate(divisions);
	}

	private static <I extends RegularTourneyEntity> TourneyEntryIterator<I> iterate(List<I> divisions) {
		final TourneyEntryIterator<I> res = new TourneyEntryIterator<>();
		for (I item : divisions) {
			Tourney tourney;
			if (item instanceof TourneyDivision) {
				tourney = ((TourneyDivision) item).getTourney();
			} else if (item instanceof TourneyRound) {
				tourney = ((TourneyRound) item).getDivision().getTourney();
			} else if (item instanceof TourneyGroup) {
				tourney = ((TourneyGroup) item).getRound().getDivision().getTourney();
			} else {
				throw new IllegalArgumentException("Unsupported item type: " + item.getClass());
			}

			List<I> iList = res.itemsMap.get(tourney);
			if (iList == null) {
				iList = new ArrayList<>();
				res.itemsMap.put(tourney, iList);
			}
			iList.add(item);
		}
		return res;
	}

	public static class TourneyComparator implements Comparator<Tourney> {
		public static final TourneyComparator ASK = new TourneyComparator(true);
		public static final TourneyComparator DESC = new TourneyComparator(false);

		private final boolean ask;

		public TourneyComparator(boolean ask) {
			this.ask = ask;
		}

		@Override
		public int compare(Tourney o1, Tourney o2) {
			int i = o1.getNumber() - o2.getNumber();
			return ask ? i : -i;
		}
	}
}
