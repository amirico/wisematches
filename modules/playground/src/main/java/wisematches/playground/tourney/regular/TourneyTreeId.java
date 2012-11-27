package wisematches.playground.tourney.regular;

import java.util.*;

/**
 * Wraps collection of {@code TourneyGroup}s to tree view grouped by Id.
 * <p/>
 * This is light version of {@link TourneyTree} view because doesn't load anything from DB and just use
 * {@code TourEntity.Id} object as a key.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 * @see TourneyTree
 */
public class TourneyTreeId {
	private final Map<Tourney.Id, List<TourneyDivision.Id>> divisionMap = new HashMap<>();
	private final Map<TourneyDivision.Id, List<TourneyRound.Id>> roundMap = new HashMap<>();
	private final Map<TourneyRound.Id, List<TourneyGroup>> groupMap = new HashMap<>();

	public TourneyTreeId(Collection<TourneyGroup> groups) {
		for (TourneyGroup group : groups) {
			final TourneyRound.Id roundId = group.getId().getRoundId();
			final TourneyDivision.Id divisionId = roundId.getDivisionId();
			final Tourney.Id tourneyId = divisionId.getTourneyId();

			List<TourneyGroup> groupList = groupMap.get(roundId);
			if (groupList == null) {
				groupList = new ArrayList<>();
				groupMap.put(roundId, groupList);
			}
			groupList.add(group);

			List<TourneyRound.Id> roundList = roundMap.get(divisionId);
			if (roundList == null) {
				roundList = new ArrayList<>();
				roundMap.put(divisionId, roundList);
			}
			roundList.add(roundId);

			List<TourneyDivision.Id> divisionList = divisionMap.get(tourneyId);
			if (divisionList == null) {
				divisionList = new ArrayList<>();
				divisionMap.put(tourneyId, divisionList);
			}
			divisionList.add(divisionId);
		}
	}

	public Collection<Tourney.Id> getTourneys() {
		return divisionMap.keySet();
	}

	public List<TourneyDivision.Id> getDivisions(Tourney.Id tourney) {
		return divisionMap.get(tourney);
	}

	public List<TourneyRound.Id> getRounds(TourneyDivision.Id division) {
		return roundMap.get(division);
	}

	public List<TourneyGroup> getGroups(TourneyRound.Id round) {
		return groupMap.get(round);
	}
}
