package wisematches.server.web.controllers.playground.scribble.form;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum CreateScribbleTab {
	ROBOT("robot"),
	WAIT("wait"),
	CHALLENGE("challenge");

	private final String typeName;

	CreateScribbleTab(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeName() {
		return typeName;
	}
}
