package wisematches.playground.tourney.regular;

import java.util.*;

/**
 * Wraps collection of {@code TourneyGroup}s to tree view.
 * <p/>
 * It's very hard wrapper that reads all tourney objects from database. It's not recommened to use this wrapper. Please
 * check {@link TourneyTreeId} class instead.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 * @see TourneyTreeId
 */
public class TourneyTree {
	private final Map<Tourney, List<TourneyDivision>> divisionMap;
	private final Map<TourneyDivision, List<TourneyRound>> roundMap;
	private final Map<TourneyRound, List<TourneyGroup>> groupMap;

	public TourneyTree(TourneyDivision... divisions) {
		groupMap = null;
		roundMap = null;
		divisionMap = new HashMap<>();

		for (TourneyDivision division : divisions) {
			final Tourney tourney = division.getTourney();

			List<TourneyDivision> divisionList = divisionMap.get(tourney);
			if (divisionList == null) {
				divisionList = new ArrayList<>();
				divisionMap.put(tourney, divisionList);
			}
			divisionList.add(division);
		}
	}

	public TourneyTree(TourneyRound... rounds) {
		groupMap = null;
		roundMap = new HashMap<>();
		divisionMap = new HashMap<>();

		for (TourneyRound round : rounds) {
			final TourneyDivision division = round.getDivision();
			final Tourney tourney = division.getTourney();

			List<TourneyRound> roundList = roundMap.get(division);
			if (roundList == null) {
				roundList = new ArrayList<>();
				roundMap.put(division, roundList);
			}
			roundList.add(round);

			List<TourneyDivision> divisionList = divisionMap.get(tourney);
			if (divisionList == null) {
				divisionList = new ArrayList<>();
				divisionMap.put(tourney, divisionList);
			}
			divisionList.add(division);
		}
	}

	public TourneyTree(TourneyGroup... groups) {
		groupMap = new HashMap<>();
		roundMap = new HashMap<>();
		divisionMap = new HashMap<>();

		for (TourneyGroup group : groups) {
			final TourneyRound round = group.getRound();
			final TourneyDivision division = round.getDivision();
			final Tourney tourney = division.getTourney();

			List<TourneyGroup> groupList = groupMap.get(round);
			if (groupList == null) {
				groupList = new ArrayList<>();
				groupMap.put(round, groupList);
			}
			groupList.add(group);

			List<TourneyRound> roundList = roundMap.get(division);
			if (roundList == null) {
				roundList = new ArrayList<>();
				roundMap.put(division, roundList);
			}
			roundList.add(round);

			List<TourneyDivision> divisionList = divisionMap.get(tourney);
			if (divisionList == null) {
				divisionList = new ArrayList<>();
				divisionMap.put(tourney, divisionList);
			}
			divisionList.add(division);
		}
	}

	public Collection<Tourney> getTourneys() {
		return divisionMap.keySet();
	}

	public List<TourneyDivision> getDivisions(Tourney tourney) {
		return divisionMap.get(tourney);
	}

	public List<TourneyRound> getRounds(TourneyDivision division) {
		if (roundMap == null) {
			throw new IllegalStateException("Rounds list is not provided");
		}
		return roundMap.get(division);
	}

	public List<TourneyGroup> getGroups(TourneyRound round) {
		if (groupMap == null) {
			throw new IllegalStateException("Rounds list is not provided");
		}
		return groupMap.get(round);
	}
}
