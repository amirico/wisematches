package wisematches.server.services.award.impl.assembly;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.core.PersonalityManager;
import wisematches.core.Player;
import wisematches.playground.tourney.TourneyPlace;
import wisematches.playground.tourney.TourneyWinner;
import wisematches.playground.tourney.regular.*;
import wisematches.server.services.award.AwardWeight;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TourneyJudicialAssembly extends AbstractJudicialAssembly {
	private PersonalityManager personalityManager;
	private RegularTourneyManager tourneyManager;
	private final TheAwardsListener listener = new TheAwardsListener();

	protected final Log log = LogFactory.getLog("wisematches.playground.awards.tourney");

	public TourneyJudicialAssembly() {
	}

	protected void processTourneyFinished(long pid, Tourney tourney, TourneyPlace place) {
		AwardWeight weight = null;
		switch (place) {
			case THIRD:
				weight = AwardWeight.BRONZE;
				break;
			case SECOND:
				weight = AwardWeight.SILVER;
				break;
			case FIRST:
				weight = AwardWeight.GOLD;
				break;
		}
		if (weight != null) {
			final Player player = personalityManager.getPlayer(pid);
			grantAward(player, "tourney.winner", weight, new TourneyRelationship(tourney.getNumber()));
		} else {
			log.error("TourneyPlace can't be converted to AwardWeight: " + place);
		}
	}

	public void setPersonalityManager(PersonalityManager personalityManager) {
		this.personalityManager = personalityManager;
	}

	public void setTourneyManager(RegularTourneyManager tourneyManager) {
		if (this.tourneyManager != null) {
			this.tourneyManager.removeRegularTourneyListener(listener);
		}

		this.tourneyManager = tourneyManager;

		if (this.tourneyManager != null) {
			this.tourneyManager.addRegularTourneyListener(listener);
		}
	}

	private final class TheAwardsListener implements RegularTourneyListener {
		private TheAwardsListener() {
		}

		@Override
		public void tourneyAnnounced(Tourney tourney) {
		}

		@Override
		public void tourneyStarted(Tourney tourney) {
		}

		@Override
		public void tourneyFinished(Tourney tourney, TourneyDivision division) {
			for (TourneyWinner winner : division.getTourneyWinners()) {
				processTourneyFinished(winner.getPlayer(), tourney, winner.getPlace());
			}
		}
	}
}
