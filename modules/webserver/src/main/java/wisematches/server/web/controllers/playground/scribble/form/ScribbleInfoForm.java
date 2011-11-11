package wisematches.server.web.controllers.playground.scribble.form;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleInfoForm {
	private long id;
	private String title;
	private PlayerInfoForm[] playersInfo;

	public ScribbleInfoForm(long id, String title, PlayerInfoForm[] playersInfo) {
		this.id = id;
		this.title = title;
		this.playersInfo = playersInfo;
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public PlayerInfoForm[] getPlayersInfo() {
		return playersInfo;
	}
}
