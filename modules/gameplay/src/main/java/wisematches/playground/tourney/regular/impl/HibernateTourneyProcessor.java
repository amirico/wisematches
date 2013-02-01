package wisematches.playground.tourney.regular.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.LongType;
import wisematches.core.Language;
import wisematches.core.PersonalityManager;
import wisematches.playground.*;
import wisematches.playground.tourney.TourneyPlace;
import wisematches.playground.tourney.regular.*;
import wisematches.playground.tourney.regular.impl.referee.FinalGroupResultReferee;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class HibernateTourneyProcessor {
	private TourneyReferee tourneyReferee = new FinalGroupResultReferee();

	private static final Log log = LogFactory.getLog("wisematches.server.playground.tourney.regular");

	HibernateTourneyProcessor() {
	}

	void initiateTourneys(Session session, Collection<RegularTourneyListener> tourneyListeners, Collection<RegistrationListener> subscriptionListeners) {
		final Query query = session.createQuery("from HibernateTourney where scheduledDate<=:scheduled and startedDate is null");
		query.setParameter("scheduled", new Date());

		final List list = query.list();
		if (list.size() > 0) {
			log.info("Initialize tourneys: " + list.size());
		}
		for (Object o : list) {
			final HibernateTourney tourney = (HibernateTourney) o;
			log.info("Start initialisation of tourney: " + tourney.getId());
			final int tourneyNumber = tourney.getNumber();
			final Collection<TourneyResubscription> resubscriptions = resortRegistrations(session, tourneyNumber, 1);

			if (resubscriptions.size() != 0) {
				log.info("Some players were sectionChanged: " + resubscriptions);
			} else {
				log.info("No resubscriptions for this tourney");
			}
			final Collection<HibernateTourneyDivision> divisions = new ArrayList<>();
			final RegistrationsSummaryImpl subscriptions = HibernateQueryHelper.getRegistrationsSummary(session, tourneyNumber, 1);
			// create divisions
			for (Language language : Language.values()) {
				for (TourneySection section : TourneySection.values()) {
					if (subscriptions.hasPlayers(language, section)) {
						final HibernateTourneyDivision division = new HibernateTourneyDivision(tourney, language, section);
						divisions.add(division);
						session.save(division);
					}
				}
			}

			log.info("Initiated divisions: " + divisions);

			tourney.startTourney();
			if (subscriptions.getPlayers() == 0) { // no subscription - finish the tourney right now
				tourney.finishTourney();
			}
			session.update(tourney);

			// notify players
			for (TourneyResubscription resubscription : resubscriptions) {
				final long player = resubscription.getPlayer();
				final TourneySection oldSection = resubscription.getOldSection();
				final TourneySection newSection = resubscription.getNewSection();
				for (RegistrationListener listener : subscriptionListeners) {
					listener.sectionChanged(player, tourneyNumber, oldSection, newSection);
				}
			}

			// notify about new tourney
			for (RegularTourneyListener listener : tourneyListeners) {
				listener.tourneyStarted(tourney);
			}
			log.info("Tourney was initialised");
		}
	}

	<S extends GameSettings> void initiateDivisions(Session session, GamePlayManager<S, ?> gamePlayManager, GameSettingsProvider<S, TourneyGroup> settingsProvider, PersonalityManager playerManager) throws BoardCreationException {
		final Query query = session.createQuery("from HibernateTourneyDivision d where d.activeRound = 0 and d.finishedDate is null");
		for (Object o : query.list()) {
			final HibernateTourneyDivision division = (HibernateTourneyDivision) o;
			log.info("Initiating finished division: " + division.getId());

			final Query roundsCountQuery = session.createQuery("select max(r.round) from HibernateTourneyRound r where r.division=:division");
			roundsCountQuery.setParameter("division", division);
			final Object roundsCountValue = roundsCountQuery.uniqueResult();
			final int nextRoundNumber = roundsCountValue == null ? 1 : ((Number) roundsCountValue).intValue() + 1;
			final Collection<Long> subscribedPlayers = getRegisteredPlayers(session, division, nextRoundNumber);
			if (subscribedPlayers.size() <= 1) {
				log.error("Broken registered players count! We can get winners from previous round and previous was last.");
			}
			log.info("Next round number: " + nextRoundNumber + ", registered players: " + subscribedPlayers.size());
			final HibernateTourneyRound round = new HibernateTourneyRound(division, nextRoundNumber);
			session.save(round);

			int roundGamesCount = 0;
			final List<long[]> longs = splitByGroups(subscribedPlayers);
			for (int i = 0, longsSize = longs.size(); i < longsSize; i++) {
				final HibernateTourneyGroup group = new HibernateTourneyGroup(i + 1, round, longs.get(i));
				session.save(group);
				roundGamesCount += group.initializeGames(gamePlayManager, settingsProvider, playerManager);
				session.update(group);
			}
			round.gamesStarted(roundGamesCount);
			session.update(round);

			division.startRound(round);
			session.update(division);
		}
	}

	void finalizeDivisions(Session session, GameBoard<?, ?> board, Collection<RegistrationListener> subscriptionListeners, Collection<RegularTourneyListener> tourneyListeners) {
		final HibernateTourneyGroup group = getGroupByBoard(session, board);
		if (group != null) {
			log.info("Finalize tourney group: " + group);
			group.finalizeGame(board);
			session.update(group);

			final HibernateTourneyRound round = group.getRound();
			round.gamesFinished(1);
			session.update(round);

			final HibernateTourneyDivision division = round.getDivision();
			if (round.getFinishedDate() != null) { // finished
				log.info("Finalize tourney round: " + round);
				if (division.finishRound(round)) {
					log.info("Finalize tourney division: " + division);
					division.finishDivision(tourneyReferee.getWinnersList(group, round, division));

					for (RegularTourneyListener listener : tourneyListeners) {
						listener.tourneyFinished(division.getTourney(), division);
					}
				}
				session.update(division);
			}

			// if group finished and not final round
			if (group.getFinishedDate() != null && !round.isFinal()) {
				final List<HibernateTourneyWinner> winnersList = tourneyReferee.getWinnersList(group, round, division);
				for (HibernateTourneyWinner winner : winnersList) {
					if (winner.getPlace() == TourneyPlace.FIRST) { // move only winners to next round
						final TourneyRound.Id roundId = round.getId();
						final TourneyDivision.Id divisionId = roundId.getDivisionId();
						final Tourney.Id tourneyId = divisionId.getTourneyId();
						final int number = tourneyId.getNumber();

						final HibernateRegistrationRecord s = new HibernateRegistrationRecord(number, winner.getPlayer(), round.getRound() + 1, divisionId.getLanguage(), divisionId.getSection());
						session.save(s);

						log.info("Subscribe player to next round: " + s);

						for (RegistrationListener listener : subscriptionListeners) {
							listener.registered(s, "won.tourney.group");
						}
					}
				}
			}
		}
	}

	void finalizeTourneys(Session session) {
		// NOTE: works for MySQL. Others DB must be checked before
		final Query query = session.createQuery("select d.tourney from HibernateTourneyDivision d where d.tourney.finishedDate is null group by d.tourney having count(*)=count(d.finishedDate)");
		for (Object o : query.list()) {
			final HibernateTourney t = (HibernateTourney) o;
			t.finishTourney();

			log.info("Finalize tourney: " + t);
		}
	}

	public void clearRegistrations(Session session) {
		final Query query = session.createSQLQuery("" +
				"DELETE s.* " +
				"FROM " +
				"tourney_regular_subs s LEFT JOIN " +
				"tourney_regular_division d ON s.roundNumber<=d.activeRound AND s.language=d.language AND s.section=d.section LEFT JOIN " +
				"tourney_regular t ON t.id=d.tourneyId AND s.tourneyNumber=t.tourneyNumber " +
				"WHERE d.started IS NOT null AND d.finished IS null");

		final int i = query.executeUpdate();
		log.info("Clear not required registrations: " + i);
	}

	Collection<TourneyResubscription> resortRegistrations(Session session, int tourney, int round) {
		final Collection<TourneyResubscription> res = new ArrayList<>();

		final Map<Long, TourneySection> originalSection = new HashMap<>();
		final Map<Long, TourneySection> resubscribedSection = new HashMap<>();

		final RegistrationsSummaryImpl subscriptions = HibernateQueryHelper.getRegistrationsSummary(session, tourney, round);
		for (Language language : Language.values()) {
			for (TourneySection section : TourneySection.values()) {
				// nothing to do if no subscription
				final int playersCount = subscriptions.getPlayers(language, section);
				if (playersCount == 0) {
					continue;
				}

				final Set<Long> invalidPlayers = new HashSet<>();
				if (playersCount != 1) {
					final Query namedQuery = session.createSQLQuery("SELECT playerId " +
							"FROM scribble_statistic s, tourney_regular_subs t " +
							"WHERE s.playerId=t.player AND t.tourneyNumber=:tourney " +
							"AND t.roundNumber=:round AND t.language=:language " +
							"AND t.section=:section AND s.rating>:rating");
					namedQuery.setParameter("round", 1);
					namedQuery.setParameter("rating", section.getTopRating());
					namedQuery.setParameter("section", section.ordinal());
					namedQuery.setParameter("language", language.ordinal());
					namedQuery.setParameter("tourney", subscriptions.getTourney());

					// select all
					for (Object ps : namedQuery.list()) { // result is BigInteger instead of long
						invalidPlayers.add(((Number) ps).longValue());
					}
				}

				// if only one - move to next
				if (playersCount - invalidPlayers.size() == 1) {
					Query query = session.createQuery("select id.player " +
							"from HibernateRegistrationRecord " +
							"where id.tourney=:tourney and id.round=1 and language=:language and section=:section");
					query.setParameter("section", section);
					query.setParameter("language", language);
					query.setInteger("tourney", subscriptions.getTourney());

					for (Object o : query.list()) {
						final Number n = (Number) o;
						invalidPlayers.add(n.longValue());
					}
				}

				if (invalidPlayers.size() != 0) {
					// remove from stats
					subscriptions.setPlayers(language, section, playersCount - invalidPlayers.size());

					Query query;
					// search next not empty section
					TourneySection nextSection = section.getHigherSection();
					while (nextSection != null && invalidPlayers.size() < 2 && subscriptions.getPlayers(language, nextSection) == 0) {
						nextSection = nextSection.getHigherSection();
					}

					if (nextSection != null) { // and move to next group
						query = session.createQuery("update from HibernateRegistrationRecord set section=:section " +
								"where id.tourney=:tourney and id.round=1 and language=:language and id.player in (:players)");
						query.setParameter("section", nextSection);
					} else { // or cancel subscription
						query = session.createQuery("delete from HibernateRegistrationRecord " +
								"where id.tourney=:tourney and id.round=1 and language=:language and id.player in (:players)");
					}
					query.setParameter("language", language);
					query.setInteger("tourney", subscriptions.getTourney());
					query.setParameterList("players", invalidPlayers, LongType.INSTANCE);

					final int count = query.executeUpdate();
					if (nextSection != null) {
						subscriptions.setPlayers(language, nextSection, subscriptions.getPlayers(language, nextSection) + count);
					}

					for (Long pid : invalidPlayers) {
						// only if new. Otherwise - reuse old
						if (!originalSection.containsKey(pid)) {
							originalSection.put(pid, section);
						}
						resubscribedSection.put(pid, nextSection);
					}
				}
			}
		}

		for (Long resubscribedPlayer : originalSection.keySet()) {
			final TourneySection oldSection = originalSection.get(resubscribedPlayer);
			final TourneySection newSection = resubscribedSection.get(resubscribedPlayer);
			res.add(new TourneyResubscription(resubscribedPlayer, oldSection, newSection));
		}
		return res;
	}

	Collection<Long> getRegisteredPlayers(Session session, HibernateTourneyDivision division, int round) {
		final Query subscriptionQuery = session.createQuery("" +
				"select s.id.player " +
				"from HibernateRegistrationRecord s " +
				"where s.id.round=:round and s.id.tourney=:tourney " +
				"and s.language=:language and s.section = :section");
		subscriptionQuery.setInteger("round", round);
		subscriptionQuery.setInteger("tourney", division.getTourney().getNumber());
		subscriptionQuery.setParameter("section", division.getSection());
		subscriptionQuery.setParameter("language", division.getLanguage());

		final List list = subscriptionQuery.list();
		final Collection<Long> res = new ArrayList<>(list.size());
		for (Object o : list) {
			res.add(((Number) o).longValue());
		}
		return res;
	}

	HibernateTourneyGroup getGroupByBoard(Session session, GameBoard<?, ?> board) {
		final Query query = session.createQuery("from HibernateTourneyGroup g where " +
				"g.game0=:game or g.game1 = :game or g.game2 = :game or " +
				"g.game3 = :game or g.game4 = :game or g.game5 = :game");
		query.setParameter("game", board.getBoardId());
		return (HibernateTourneyGroup) query.uniqueResult();
	}

	List<long[]> splitByGroups(Collection<Long> players) {
		int elapsedCount = players.size();
		final Iterator<Long> iterator = players.iterator();
		final List<long[]> res = new ArrayList<>();
		while (elapsedCount != 0) {
			if (elapsedCount >= 12) {
				res.add(takeNextPlayers(iterator, 4));
				elapsedCount -= 4;
			} else if (elapsedCount == 4 || elapsedCount == 7 || elapsedCount == 8 || elapsedCount == 10 || elapsedCount == 11) {
				res.add(takeNextPlayers(iterator, 4));
				elapsedCount -= 4;
			} else if (elapsedCount == 3 || elapsedCount == 5 || elapsedCount == 6 || elapsedCount == 9) {
				res.add(takeNextPlayers(iterator, 3));
				elapsedCount -= 3;
			} else if (elapsedCount == 2) {
				res.add(takeNextPlayers(iterator, 2));
				elapsedCount -= 2;
			}
		}

		if (iterator.hasNext()) {
			throw new IllegalStateException("After processing we still have players in iterator");
		}
		return res;
	}

	static long[] takeNextPlayers(Iterator<Long> players, int count) {
		final long[] res = new long[count];
		for (int i = 0; i < res.length; i++) {
			res[i] = players.next();
		}
		return res;
	}
}