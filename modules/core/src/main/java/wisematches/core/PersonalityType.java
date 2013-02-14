package wisematches.core;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Deprecated
public enum PersonalityType {
	ROBOT,
	VISITOR,
	PLAYER;

	public boolean isRobot() {
		return this == ROBOT;
	}

	public boolean isVisitor() {
		return this == VISITOR;
	}

	public boolean isPlayer() {
		return this == PLAYER;
	}
}
