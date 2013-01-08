package wisematches.personality;

/**
 * Each player has one of predefined membership listed in this enumeration.
 * <p/>
 * According to the membership some features can be limited or disable for the player.
 * <p/>
 * Each membership has a few constrains, like number of simultaneous games, ads visibility and so on.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum Membership implements Comparable<Membership> {
    /**
     * Indicates that the player is a robot.
     */
    ROBOT(true),
    /**
     * Indicates that the player is a guest.
     */
    GUEST(true),
    /**
     * This is regular player without any specific features.
     */
    BASIC(true),
    /**
     * Silver player. It's more well than the <i>GUEST<i> player.
     */
    SILVER(false),
    /**
     * Gold player. It's more well than the <i>GOLD<i> player.
     */
    GOLD(false),
    /**
     * o
     * Platinum player. A player with this membership has full access.
     */
    PLATINUM(false);

    private final boolean adsVisible;

    Membership(boolean adsVisible) {
        this.adsVisible = adsVisible;
    }

    /**
     * Indicates is ads should be visible or not.
     *
     * @return {@code true} if ads should be visible for this player; {@code false} - otherwise.
     */
    public boolean isAdsVisible() {
        return adsVisible;
    }

    public boolean isRobot() {
        return this == ROBOT;
    }

    public boolean isGuest() {
        return this == GUEST;
    }

    public boolean isMember() {
        return !isRobot() && !isGuest();
    }
}