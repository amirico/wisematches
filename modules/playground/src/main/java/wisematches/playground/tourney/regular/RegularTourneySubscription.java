package wisematches.playground.tourney.regular;

import wisematches.playground.tourney.TourneySubscription;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RegularTourneySubscription implements
		TourneySubscription<RegularTourneySubscription, RegularTourneySubscription.Id, RegularTourneySubscription.Context>,
		RegularTourneyEntity<RegularTourneySubscription, RegularTourneySubscription.Id, RegularTourneySubscription.Context> {

	public RegularTourneySubscription() {
	}

	@Override
	public long getPlayer() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public int getTournament() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public Id getId() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	public static class Id implements RegularTourneyEntity.Id<RegularTourneySubscription, Id> {
	}

	public static class Context implements RegularTourneyEntity.Context<RegularTourneySubscription, Context> {
	}
}