package wisematches.server.web.controllers.playground.scribble.form;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum OpponentType {
	ROBOT("robot"),
	WAIT("wait"),
	CHALLENGE("challenge");

	private final String typeName;

	OpponentType(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeName() {
		return typeName;
	}
}
