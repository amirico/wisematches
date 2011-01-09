package wisematches.server.core.account.impl;

import wisematches.kernel.notification.PlayerNotification;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public enum MockPlayerNotification1 implements PlayerNotification {
    V1,
    V2;

    public String type() {
        return "TYPE1";
    }

    public boolean isOnlineNotification() {
        return false;
    }
}
