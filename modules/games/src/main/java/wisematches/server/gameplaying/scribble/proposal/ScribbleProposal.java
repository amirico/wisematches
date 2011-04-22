package wisematches.server.gameplaying.scribble.proposal;

import wisematches.server.gameplaying.propose.GameProposal;
import wisematches.server.gameplaying.scribble.board.ScribbleSettings;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface ScribbleProposal extends GameProposal<ScribbleSettings> {
	//	private String language;
//
//	private static final long serialVersionUID = 3454838446204714710L;
//
//	protected ScribbleProposal() {
//	}
//
//	public ScribbleProposal(String title, String language, int timeLimits, int opponentsCount, Personality player, List<? extends Personality> opponents) {
//		super(title, timeLimits, opponentsCount, player, opponents);
//		this.language = language;
//	}
//
	String getLanguage();
}
