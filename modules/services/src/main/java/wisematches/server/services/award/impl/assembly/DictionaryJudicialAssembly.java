package wisematches.server.services.award.impl.assembly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wisematches.core.Player;
import wisematches.playground.tracking.Statistics;
import wisematches.playground.tracking.StatisticsListener;
import wisematches.playground.tracking.StatisticsManager;
import wisematches.server.services.award.AwardWeight;

import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DictionaryJudicialAssembly extends AbstractJudicialAssembly {
	private StatisticsManager statisticManager;

	private final StatisticsListener statisticsListener = new TheStatisticsListener();

	private static final Logger log = LoggerFactory.getLogger("wisematches.awards.DictionaryJudicial");

	public DictionaryJudicialAssembly() {
	}

	protected void processDictionaryChanged(Player requester, int approvedReclaims) {
		if (requester != null) {
			AwardWeight weight = null;
			switch (approvedReclaims) {
				case 5:
					weight = AwardWeight.BRONZE;
					break;
				case 10:
					weight = AwardWeight.SILVER;
					break;
				case 20:
					weight = AwardWeight.GOLD;
					break;
			}
			if (weight != null) {
				grantAward(requester, "dictionary.editor", weight, null);
			}
		} else {
			log.error("Very strange, unknown player changed dictionary: {}", requester);
		}
	}

	public void setStatisticManager(StatisticsManager statisticManager) {
		if (this.statisticManager != null) {
			this.statisticManager.removeStatisticsListener(statisticsListener);
		}

		this.statisticManager = statisticManager;

		if (this.statisticManager != null) {
			this.statisticManager.addStatisticsListener(statisticsListener);
		}
	}

	private final class TheStatisticsListener implements StatisticsListener {
		private TheStatisticsListener() {
		}

		@Override
		public void playerStatisticUpdated(Player player, Statistics statistic, Set<String> changes) {
			if (changes.contains("reclaimsApproved")) {
				processDictionaryChanged(player, statistic.getReclaimsApproved());
			}
		}
	}
}
