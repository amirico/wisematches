package wisematches.server.core.account.impl;

import wisematches.kernel.notification.PlayerNotification;

public enum MockPlayerNotification2 implements PlayerNotification {
    V1,
    V2;

    public String type() {
        return "TYPE2";
    }

    public boolean isOnlineNotification() {
        return false;
    }
}
