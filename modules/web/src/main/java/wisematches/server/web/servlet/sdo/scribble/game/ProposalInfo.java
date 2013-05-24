package wisematches.server.web.servlet.sdo.scribble.game;

import wisematches.core.Player;
import wisematches.playground.GameMessageSource;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.propose.ProposalType;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.server.services.state.PlayerStateManager;
import wisematches.server.web.servlet.sdo.InternationalisedInfo;
import wisematches.server.web.servlet.sdo.person.PersonalityInfo;
import wisematches.server.web.servlet.sdo.scribble.SettingsInfo;
import wisematches.server.web.servlet.sdo.time.TimeInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ProposalInfo extends InternationalisedInfo {
	private final PlayerStateManager stateManager;
	private final GameProposal<ScribbleSettings> proposal;
	private final Collection<ViolationInfo> violations;

	public ProposalInfo(GameProposal<ScribbleSettings> proposal, Collection<ViolationInfo> violations, PlayerStateManager stateManager, GameMessageSource messageSource, Locale locale) {
		super(messageSource, locale);
		this.stateManager = stateManager;
		this.proposal = proposal;
		this.violations = violations;
	}

	public long getId() {
		return proposal.getId();
	}

	public SettingsInfo getSettings() {
		return new SettingsInfo(proposal.getSettings(), null, messageSource, locale);
	}

	public TimeInfo getCreationDate() {
		return new TimeInfo(proposal.getCreationDate(), messageSource, locale);
	}

	public long getInitiator() {
		return proposal.getInitiator().getId();
	}

	public List<PersonalityInfo> getPlayers() {
		final List<Player> players = proposal.getPlayers();

		final List<PersonalityInfo> res = new ArrayList<>(players.size());
		for (Player player : players) {
			if (player == null) {
				res.add(null);
			} else {
				res.add(new PersonalityInfo(messageSource.getPersonalityNick(player, locale), player, stateManager));
			}
		}
		return res;
	}

	public List<Long> getJoinedPlayers() {
		final List<Player> joinedPlayers = proposal.getJoinedPlayers();
		List<Long> res = new ArrayList<>(joinedPlayers.size());
		for (Player joinedPlayer : joinedPlayers) {
			res.add(joinedPlayer.getId());
		}
		return res;
	}

	public boolean isReady() {
		return proposal.isReady();
	}

	public ProposalType getProposalType() {
		return proposal.getProposalType();
	}

	public Collection<ViolationInfo> getViolations() {
		return violations;
	}
}
