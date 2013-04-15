package wisematches.server.services.award.impl.assembly;

import wisematches.core.Player;
import wisematches.core.RobotType;
import wisematches.playground.tracking.Statistics;
import wisematches.playground.tracking.StatisticsListener;
import wisematches.playground.tracking.StatisticsManager;
import wisematches.server.services.award.AwardWeight;

import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RobotJudicialAssembly extends AbstractJudicialAssembly {
	private StatisticsManager statisticManager;
	private final TheAwardsListener listener = new TheAwardsListener();

	public RobotJudicialAssembly() {
	}

	protected void processStatisticUpdated(Player person, Set<String> changes, Statistics statistics) {
		final int dullWins = statistics.getRobotWins(RobotType.DULL);
		final int traineeWins = statistics.getRobotWins(RobotType.TRAINEE);
		final int expertsWins = statistics.getRobotWins(RobotType.EXPERT);
		if (changes.contains("robotWinsDull")) {
			if (dullWins == 10) {
				grantAward(person, "robot.conqueror", AwardWeight.BRONZE, null);

				if (traineeWins >= 5) {
					grantAward(person, "robot.conqueror", AwardWeight.SILVER, null);

					if (expertsWins >= 1) {
						grantAward(person, "robot.conqueror", AwardWeight.GOLD, null);
					}
				}
			}
		} else if (changes.contains("robotWinsTrainee")) {
			if (dullWins >= 10 && traineeWins == 5) {
				grantAward(person, "robot.conqueror", AwardWeight.SILVER, null);

				if (expertsWins >= 1) {
					grantAward(person, "robot.conqueror", AwardWeight.BRONZE, null);
				}
			}
		} else if (changes.contains("robotWinsExpert")) {
			if (dullWins >= 10 && traineeWins >= 5 && expertsWins == 1) {
				grantAward(person, "robot.conqueror", AwardWeight.GOLD, null);
			}
		}
	}

	public void setStatisticManager(StatisticsManager statisticManager) {
		if (this.statisticManager != null) {
			this.statisticManager.removeStatisticsListener(listener);
		}

		this.statisticManager = statisticManager;

		if (this.statisticManager != null) {
			this.statisticManager.addStatisticsListener(listener);
		}
	}

	private final class TheAwardsListener implements StatisticsListener {
		private TheAwardsListener() {
		}

		@Override
		public void playerStatisticUpdated(Player player, Statistics statistic, Set<String> changes) {
			processStatisticUpdated(player, changes, statistic);
		}
	}
}
