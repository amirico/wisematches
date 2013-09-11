package wisematches.server.web.servlet.sdo.scribble.game;

import wisematches.server.web.servlet.sdo.scribble.board.DescriptionInfo;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ActiveGamesInfo {
	private final Collection<DescriptionInfo> games;
	private final Collection<ProposalInfo> proposals;

	public ActiveGamesInfo(Collection<DescriptionInfo> games, Collection<ProposalInfo> proposals) {
		this.games = games;
		this.proposals = proposals;
	}

	public Collection<DescriptionInfo> getGames() {
		return games;
	}

	public Collection<ProposalInfo> getProposals() {
		return proposals;
	}
}
