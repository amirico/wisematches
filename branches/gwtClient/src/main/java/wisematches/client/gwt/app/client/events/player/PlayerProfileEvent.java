package wisematches.client.gwt.app.client.events.player;

/**
 * Indicates that player's profile was changed.
 * <p/>
 * This is empty event and informatin about changed player should be taken from server using service.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerProfileEvent extends PlayerStateEvent {
    /**
     * This is GWT Serialization only constructor.
     */
    @Deprecated
    public PlayerProfileEvent() {
    }

    public PlayerProfileEvent(long playerId) {
        super(playerId);
    }
}
