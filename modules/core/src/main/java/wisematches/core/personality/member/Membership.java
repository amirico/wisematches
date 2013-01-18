package wisematches.core.personality.member;

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
	 * This is regular player without any specific features.
	 */
	DEFAULT,

	/**
	 * Silver player. It's more well than the <i>GUEST<i> player.
	 */
	SILVER,

	/**
	 * Gold player. It's more well than the <i>GOLD<i> player.
	 */
	GOLD,

	/**
	 * o
	 * Platinum player. A player with this membership has full access.
	 */
	PLATINUM;

	private final String code;

	Membership() {
		code = name().toLowerCase();
	}

	/**
	 * Returns code of this membership. It's, de facto, lower case name.
	 *
	 * @return the code of this membership. It's, de facto, lower case name.
	 */
	public String getCode() {
		return code;
	}

	public boolean isDefault() {
		return this == DEFAULT;
	}
}