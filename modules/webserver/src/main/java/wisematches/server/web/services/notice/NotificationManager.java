package wisematches.server.web.services.notice;

import wisematches.personality.Personality;

/**
 * This class allows manager notification and check they state.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationManager {
    /**
     * Checks is notification with specified name enabled for specified player.
     *
     * @param name        the notification name
     * @param personality the person who's notification should be checked.
     * @return {@code true} if notification enabled; {@code false} - otherwise.
     */
    boolean isNotificationEnabled(String name, Personality personality);

    /**
     * Checks is notification enabled for specified array of players.
     *
     * @param name          the notification name.
     * @param personalities the array of players.
     * @return the array of notification's state in the same order as {@code personalities} order.
     */
    boolean[] isNotificationEnabled(String name, Personality[] personalities);

    /**
     * Returns notification mask for specified personality.
     *
     * @param personality the personality who's mask should be returned.
     * @return the notification's mask.
     */
    NotificationMask getNotificationMask(Personality personality);

    /**
     * Updates notifications mask for specified player.
     *
     * @param personality  the player who's notifications mask should be updated.
     * @param notification the new notification's mask.
     */
    void setNotificationMask(Personality personality, NotificationMask notification);
}
