package wisematches.client.gwt.core.client.events;

/**
 * If event is extends this interface it means that it has some correlation with other events
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface Correlative {
    /**
     * Checks correlation of this event with specified. Each event can be:
     * <ul>
     * <li>{@code NEUTRAL} - events are not correlated.
     * <li>{@code EXCLUSION} - this event is exlusion of specified. In this case specified event should
     * not be processed.
     * <li> {@code MUTUAL_EXCLUSION} - this and specified events are exlusion each other and
     * both should not be processed.
     * </ul>
     *
     * @param event the event to be checked.
     * @return correlation between this event and specified.
     */
    Correlation eventCorrelation(Event event);
}
