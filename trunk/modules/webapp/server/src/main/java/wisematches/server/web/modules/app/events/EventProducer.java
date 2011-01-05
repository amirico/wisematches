package wisematches.server.web.modules.app.events;

/**
 * Each class that provides event to client should implements this interface.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface EventProducer {
    /**
     * Indicates that producer should be activated and events can be sent to specified notificator.
     *
     * @param notificator the event notificator. Producer can use this interface to fire events.
     */
    void activateProducer(EventNotificator notificator);

    /**
     * Indicates that producer was deactivated and it can't be sent event to notificator any more. In
     * other case {@code IllegalStateException} will be thrown from {@code fireEvent} method of
     * {@code EventNotificator}.
     *
     * @see EventNotificator#fireEvent(Event)
     */
    void deactivateProducer();
}
