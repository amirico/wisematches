package wisematches.core;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum PersonalityType {
	ROBOT(false),
	PLAYER(true),
	VISITOR(false);

	private final boolean traceable;

	private PersonalityType(boolean traceable) {
		this.traceable = traceable;
	}

	public boolean isRobot() {
		return this == ROBOT;
	}

	public boolean isPlayer() {
		return this == PLAYER;
	}

	public boolean isVisitor() {
		return this == VISITOR;
	}

	public boolean isTraceable() {
		return traceable;
	}
}