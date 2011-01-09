package wisematches.server.core.sessions;

/**
 * Methods of this interface are invoked when player creates new or removes exist session.
 * <p/>
 * One player can have a lot of sessions and methods of this interface can be invoked many times
 * for the same player.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface PlayerSessionsListener {
    /**
     * Indicates that player created a session.
     *
     * @param event the player session event.
     */
    void playerSessionsCreated(PlayerSessionsEvent event);

    /**
     * Indicates that player closed a session.
     *
     * @param event the player session event.
     */
    void playerSessionsRemoved(PlayerSessionsEvent event);
}
