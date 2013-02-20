package wisematches.core;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum PersonalityType {
	ROBOT,
	MEMBER,
	VISITOR;

	private PersonalityType() {
	}

	public boolean isRobot() {
		return this == ROBOT;
	}

	public boolean isMember() {
		return this == MEMBER;
	}

	public boolean isVisitor() {
		return this == VISITOR;
	}
}