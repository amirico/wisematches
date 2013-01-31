package wisematches.core;

/**
 * Each player has one of predefined membership listed in this enumeration.
 * <p/>
 * According to the membership some features can be limited or disable for the player.
 * <p/>
 * Each membership has a few constrains, like number of simultaneous games, ads visibility and so on.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum PlayerType implements Comparable<PlayerType> {
	/**
	 * Indicates that it's guest player.
	 */
	GUEST,

	/**
	 * Indicates that it's premium player.
	 */
	MEMBER;

	PlayerType() {
	}

	public boolean isGuest() {
		return this == GUEST;
	}

	public boolean isMember() {
		return this == MEMBER;
	}
}