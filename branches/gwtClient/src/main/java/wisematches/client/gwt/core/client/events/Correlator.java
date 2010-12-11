package wisematches.client.gwt.core.client.events;

/**
 * Interface that allows get correlation between two events.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface Correlator<T extends Event> {
    /**
     * Checks is specified event supported by this correlator or not. Only supported events
     * will be passed into {@code eventsColleration} method.
     *
     * @param e the event to be checked.
     * @return {@code true} if event is supported; {@code false} - otherwise.
     */
    boolean isEventSupported(Event e);

    /**
     * This methods check correlation {@code event1} with {@code event2}.
     *
     * @param original the first event of correlation.
     * @param checking the second event of correlation.
     * @return the result correlation. This method must not returns {@code null} result. In this case {@code Correlation#NEUTRAL} should be returned.
     */
    Correlation eventsColleration(T original, T checking);
}
