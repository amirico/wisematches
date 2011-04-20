package wisematches.server.gameplaying.scribble.room.proposal;

import wisematches.server.gameplaying.room.propose.GameProposal;
import wisematches.server.personality.Personality;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleProposal extends GameProposal {
	private String language;

	private static final long serialVersionUID = 3454838446204714710L;

	protected ScribbleProposal() {
	}

	public ScribbleProposal(String title, String language, int timeLimits, int opponentsCount, Personality player, List<? extends Personality> opponents) {
		super(title, timeLimits, opponentsCount, player, opponents);
		this.language = language;
	}

	public String getLanguage() {
		return language;
	}
}
