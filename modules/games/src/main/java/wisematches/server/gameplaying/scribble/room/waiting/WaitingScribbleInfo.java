package wisematches.server.gameplaying.scribble.room.waiting;

import wisematches.server.gameplaying.room.waiting.WaitingGameInfo;
import wisematches.server.player.Language;
import wisematches.server.player.Player;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WaitingScribbleInfo extends WaitingGameInfo {
	private Language language;

	public WaitingScribbleInfo() {
	}

	public WaitingScribbleInfo(long id, String title, int timeLimits, int playersCount, Language language, Player player) {
		super(id, title, timeLimits, playersCount, player);
		this.language = language;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}
}
