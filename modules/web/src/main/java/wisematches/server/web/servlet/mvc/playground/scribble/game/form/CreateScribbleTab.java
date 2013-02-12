package wisematches.server.web.servlet.mvc.playground.scribble.game.form;

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
