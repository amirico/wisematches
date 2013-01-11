package wisematches.playground.award.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import wisematches.personality.Personality;
import wisematches.personality.player.computer.robot.RobotType;
import wisematches.playground.GameRelationship;
import wisematches.playground.award.AwardWeight;
import wisematches.playground.tourney.TourneyPlace;
import wisematches.playground.tourney.TourneyWinner;
import wisematches.playground.tourney.regular.*;
import wisematches.playground.tracking.StatisticManager;
import wisematches.playground.tracking.Statistics;
import wisematches.playground.tracking.StatisticsListener;

import java.util.Date;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateAwardsCenter {
	private SessionFactory sessionFactory;
	private StatisticManager statisticManager;
	private RegularTourneyManager tourneyManager;

	private final TheAwardsListener listener = new TheAwardsListener();

	protected final Log log = LogFactory.getLog("wisematches.playground.awards.center");

	public HibernateAwardsCenter() {
	}

	protected void processStatisticUpdated(Personality person, Set<String> changes, Statistics statistics) {
		if (changes.contains("robotWinsDull")) {
			if (statistics.getRobotWins(RobotType.DULL) == 10) {
				fireAward(person.getId(), "robot", AwardWeight.SILVER, null);
			}
		} else if (changes.contains("robotWinsTrainee")) {
			if (statistics.getRobotWins(RobotType.DULL) >= 10 &&
					statistics.getRobotWins(RobotType.TRAINEE) == 5) {
				fireAward(person.getId(), "robot", AwardWeight.BRONZE, null);
			}
		} else if (changes.contains("robotWinsExpert")) {
			if (statistics.getRobotWins(RobotType.DULL) >= 10 &&
					statistics.getRobotWins(RobotType.TRAINEE) >= 5 &&
					statistics.getRobotWins(RobotType.EXPERT) == 1) {
				fireAward(person.getId(), "robot", AwardWeight.BRONZE, null);
			}
		}
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
			fireAward(pid, "tourney", weight, new TourneyRelationship(tourney.getNumber()));
		} else {
			log.error("TourneyPlace can't be converted to AwardWeight: " + place);
		}
	}

	private void fireAward(long pid, String code, AwardWeight weight, GameRelationship relationship) {
		final HibernateAward award = new HibernateAward(pid, code, new Date(), weight, relationship);
		sessionFactory.getCurrentSession().save(award);
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setStatisticManager(StatisticManager statisticManager) {
		if (this.statisticManager != null) {
			this.statisticManager.removeStatisticListener(listener);
		}

		this.statisticManager = statisticManager;

		if (this.statisticManager != null) {
			this.statisticManager.addStatisticListener(listener);
		}
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

	private final class TheAwardsListener implements StatisticsListener, RegularTourneyListener {
		private TheAwardsListener() {
		}

		@Override
		public void tourneyAnnounced(Tourney tourney) {
		}

		@Override
		public void tourneyStarted(Tourney tourney) {
		}

		@Override
		public void playerStatisticUpdated(Personality personality, Set<String> changes, Statistics statistic) {
			processStatisticUpdated(personality, changes, statistic);
		}

		@Override
		public void tourneyFinished(Tourney tourney, TourneyDivision division) {
			for (TourneyWinner winner : division.getTourneyWinners()) {
				processTourneyFinished(winner.getPlayer(), tourney, winner.getPlace());
			}
		}
	}
}