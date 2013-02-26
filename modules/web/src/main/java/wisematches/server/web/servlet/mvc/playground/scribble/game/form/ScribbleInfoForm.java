package wisematches.server.web.servlet.mvc.playground.scribble.game.form;

import wisematches.playground.scribble.ScribbleSettings;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Deprecated
public class ScribbleInfoForm {
	private final long id;
	private final ScribbleSettings settings;
	private final long playerTurn;
	private final String elapsedTime;
	private final PlayerInfoForm[] playersInfo;

	public ScribbleInfoForm(long id, ScribbleSettings settings, String elapsedTime, long playerTurn, PlayerInfoForm[] playersInfo) {
		this.id = id;
		this.settings = settings;
		this.playerTurn = playerTurn;
		this.elapsedTime = elapsedTime;
		this.playersInfo = playersInfo;
	}

	public long getId() {
		return id;
	}

	public ScribbleSettings getSettings() {
		return settings;
	}

	public String getElapsedTime() {
		return elapsedTime;
	}

	public long getPlayerTurn() {
		return playerTurn;
	}

	public PlayerInfoForm[] getPlayersInfo() {
		return playersInfo;
	}
}
