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
public enum Membership {
	/**
	 * Indicates that the player is a robot.
	 */
	ROBOT(0, true),
	/**
	 * Indicates that the player is a guest.
	 */
	GUEST(1, true),
	/**
	 * This is regular player without any specific features.
	 */
	BASIC(5, false),
	/**
	 * Silver player. It's more well than the <i>GUEST<i> player.
	 */
	SILVER(30, false),
	/**
	 * Gold player. It's more well than the <i>GOLD<i> player.
	 */
	GOLD(50, false),
	/**
	 * Platinum player. A player with this membership has full access.
	 */
	PLATINUM(100, false);

	private final boolean adsVisible;
	private final int simultaneousGames;

	Membership(int simultaneousGames, boolean adsVisible) {
		this.simultaneousGames = simultaneousGames;
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

	/**
	 * Returns number of simultaneous games.
	 *
	 * @return the number of simultaneous games.
	 */
	public int getSimultaneousGames() {
		return simultaneousGames;
	}
}