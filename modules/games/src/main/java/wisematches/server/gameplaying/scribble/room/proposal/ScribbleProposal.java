package wisematches.server.gameplaying.scribble.room.proposal;

import wisematches.server.gameplaying.room.propose.GameProposal;
import wisematches.server.player.Language;
import wisematches.server.player.Player;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleProposal extends GameProposal {
	private Language language;

	protected ScribbleProposal() {
	}

	public ScribbleProposal(long id, String title, int timeLimits, int playersCount, Language language, Player player) {
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
