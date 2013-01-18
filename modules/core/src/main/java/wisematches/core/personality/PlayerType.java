package wisematches.core.personality;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum PlayerType {
	GUEST,
	ROBOT,
	MEMBER;

	public boolean isRobot() {
		return this == ROBOT;
	}

	public boolean isGuest() {
		return this == GUEST;
	}

	public boolean isMember() {
		return this == MEMBER;
	}
}
