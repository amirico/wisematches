package wisematches.server.deprecated.web.rpc;

/**
 * Marker interface that indicates that {@code IRemoteService} is secure and before any methods
 * call {@code RemoteServiceHandlerAdapter} should check is player logged in or not.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface SecureRemoteService extends RemoteService {
}
