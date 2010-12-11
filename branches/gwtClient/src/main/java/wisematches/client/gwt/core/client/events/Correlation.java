package wisematches.client.gwt.core.client.events;

/**
 * Enumeration that contains types of correlation between events.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public enum Correlation {
    /**
     * Events are not correlated
     */
    NEUTRAL,
    /**
     * One event is exlusion another. In this case second event should not be processed.
     */
    EXCLUSION,
    /**
     * One and another events are exlusion each other andboth should not be processed.
     */
    MUTUAL_EXCLUSION
}
