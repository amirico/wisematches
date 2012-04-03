package wisematches.server.web.services.notify.impl.processor;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DependingNotificationProcessor extends FilteringNotificationProcessor {
    public DependingNotificationProcessor() {
        super();
    }

    /*
    			if (publisher.isStateDepending() && descriptor.isOfflineOnly() && playerStateManager.isPlayerOnline(recipient)) {
    				log.debug("Notification was ignored: incorrect player's state");
    				continue;
    			}
    */

}
