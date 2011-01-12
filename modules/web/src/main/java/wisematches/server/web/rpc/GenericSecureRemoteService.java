package wisematches.server.web.rpc;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GenericSecureRemoteService extends GenericRemoteService implements SecureRemoteService {
/*
    */
/**
 * Returns associated with this session player. This method never returns {@code null}
 * because before any method is called this controller checks that session and
 * player exist.
 *
 * @return the associated player.
 *//*

    protected Player getPlayer() {
        return getPlayerSessionBean().getPlayer();
    }

    */
/**
 * Returns {@code PlayerSessionBean} associated with this controller.
 *
 * @param <T> the result type of {@code PlayerSessionBean}.
 * @return the player session bean interface
 * @see wisematches.server.core.sessions.PlayerSessionsManager#getPlayerSessionBean(String)
 *//*

    @SuppressWarnings("unchecked")
    protected <T extends PlayerSessionBean> T getPlayerSessionBean() {
        return (T) RemoteServiceHandlerAdapter.getRemoteServiceContext().getSessionBean();
    }

    */
/**
 * Returns member type for specified player.
 *
 * @param player the player to check.
 * @return the player member type.
 *//*

*/
/*
    protected static MemberType getMemberType(Player player) {
        if (player instanceof RobotPlayer) {
            return MemberType.ROBOT;
        } else if (player instanceof GuestPlayer) {
            return MemberType.GUEST;
        }
        return MemberType.BASIC;
    }
*/

}
