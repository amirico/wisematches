package wisematches.server.web.servlet.sdo.scribble.game;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ActiveGamesInfo {
	private final Collection<GameInfo> games;
	private final Collection<ProposalInfo> proposals;

	public ActiveGamesInfo(Collection<GameInfo> games, Collection<ProposalInfo> proposals) {
		this.games = games;
		this.proposals = proposals;
	}

	public Collection<GameInfo> getGames() {
		return games;
	}

	public Collection<ProposalInfo> getProposals() {
		return proposals;
	}
}
