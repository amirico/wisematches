package wisematches.server.deprecated.web.modules.app.events;

/**
 * {@code EventNotificator} is part of {@code EventProducer} and allows event producers to fire events.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface EventNotificator {
	/**
	 * Fires speciifed event.
	 *
	 * @param event the event to be fired.
	 * @throws NullPointerException  if {@code event} is null.
	 * @throws IllegalStateException if {@code EventProducer} was deactivated but events are fired using this method.
	 */
//    void fireEvent(Event event);
}
