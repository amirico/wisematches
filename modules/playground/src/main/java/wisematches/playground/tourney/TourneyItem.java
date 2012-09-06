package wisematches.playground.tourney;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneyItem<T extends TourneyItem<T, I, C>, I extends TourneyEntity.Id<T, I>, C extends TourneyEntity.Context<T, C>> extends TourneyEntity<T, I, C> {
}
