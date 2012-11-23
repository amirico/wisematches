package wisematches.playground.tourney.regular.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import wisematches.personality.Language;
import wisematches.playground.tourney.TourneyEntity;
import wisematches.playground.tourney.regular.*;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class HibernateQueryHelper {
	private HibernateQueryHelper() {
	}

	static boolean hasScheduledTourneys(Session session, Date scheduledDate) {
		final Query query = session.createQuery("select 1 from HibernateTourney t where t.scheduledDate = :scheduledDate");
		query.setParameter("scheduledDate", scheduledDate);
		return query.uniqueResult() != null;
	}

	static int getNextTourneyNumber(Session session) {
		int number = 1;
		final Number n = (Number) session.createQuery("select max(number) from HibernateTourney ").uniqueResult();
		if (n != null) {
			number = n.intValue() + 1;
		}
		return number;
	}

	static Tourney getTourney(Session session, Tourney.Id id) {
		final Tourney.Id id1 = Tourney.Id.class.cast(id);
		final Query query = session.createQuery("from HibernateTourney t where t.number=:tourney");
		query.setInteger("tourney", id1.getNumber());
		return (Tourney) query.uniqueResult();

	}

	static TourneyDivision getDivision(Session session, TourneyDivision.Id id) {
		return (TourneyDivision) session.createQuery("from HibernateTourneyDivision d " +
				"where d.tourney.number=:tourney and d.section = :section and d.language = :language")
				.setInteger("tourney", id.getTourneyId().getNumber())
				.setParameter("section", id.getSection())
				.setParameter("language", id.getLanguage())
				.uniqueResult();
	}

	static TourneyRound getRound(Session session, TourneyRound.Id id1) {
		return (TourneyRound) session.createQuery("from HibernateTourneyRound r " +
				"where r.division.tourney.number=:tourney and r.division.section = :section " +
				"and r.division.language = :language and r.round=:round")
				.setInteger("tourney", id1.getDivisionId().getTourneyId().getNumber())
				.setParameter("section", id1.getDivisionId().getSection())
				.setParameter("language", id1.getDivisionId().getLanguage())
				.setInteger("round", id1.getRound())
				.uniqueResult();
	}

	static TourneyGroup getGroup(Session session, TourneyGroup.Id id1) {
		return (TourneyGroup) session.createQuery("from HibernateTourneyGroup g " +
				"where g.round.division.tourney.number=:tourney and g.round.division.section = :section " +
				"and g.round.division.language = :language and g.round.round=:round and g.group = :group")
				.setInteger("tourney", id1.getRoundId().getDivisionId().getTourneyId().getNumber())
				.setParameter("section", id1.getRoundId().getDivisionId().getSection())
				.setParameter("language", id1.getRoundId().getDivisionId().getLanguage())
				.setInteger("round", id1.getRoundId().getRound())
				.setInteger("group", id1.getGroup())
				.uniqueResult();
	}

	static Query searchTourneis(Session session, Tourney.Context context, boolean count) {
		return session.createQuery((count ? "select count(*) " : "") + "from HibernateTourney t " + convertStateToQuery(context.getStates(), "t", "where"));
	}

	static Query searchRounds(Session session, TourneyRound.Context context, boolean count) {
		if (context.getDivisionId() != null) {
			Query query = session.createQuery((count ? "select count(*) " : "") +
					"from HibernateTourneyRound r " +
					"where r.division.tourney.number=:tourney and " +
					"r.division.language=:language and r.division.section=:section " +
					convertStateToQuery(context.getStates(), "r", "and"));
			query.setParameter("tourney", context.getDivisionId().getTourneyId().getNumber());
			query.setParameter("language", context.getDivisionId().getLanguage());
			query.setParameter("section", context.getDivisionId().getSection());
			return query;
		} else if (context.getTourneyId() != null) {
			Query query = session.createQuery((count ? "select count(*) " : "") +
					"from HibernateTourneyRound r " +
					"where r.division.tourney.number=:tourney " +
					convertStateToQuery(context.getStates(), "r", "and"));
			query.setParameter("tourney", context.getTourneyId().getNumber());
			return query;
		} else {
			throw new IllegalArgumentException("Invalid group context: " + context);
		}
	}

	static Query searchGroups(Session session, TourneyGroup.Context context, boolean count) {
		if (context.getRoundId() != null) {
			Query query = session.createQuery((count ? "select count(*) " : "") +
					"from HibernateTourneyGroup g " +
					"where g.round.division.tourney.number=:tourney and " +
					"g.round.division.language=:language and g.round.division.section=:section " +
					"and g.round.round=:round " +
					convertStateToQuery(context.getStates(), "g", "and"));
			query.setInteger("tourney", context.getRoundId().getDivisionId().getTourneyId().getNumber());
			query.setParameter("language", context.getRoundId().getDivisionId().getLanguage());
			query.setParameter("section", context.getRoundId().getDivisionId().getSection());
			query.setInteger("round", context.getRoundId().getRound());
			return query;
		} else if (context.getPersonality() != null) {
			Query query = session.createQuery((count ? "select count(*) " : "") +
					"from HibernateTourneyGroup g " +
					"where (g.player1=:pid or g.player2=:pid or g.player3=:pid or g.player4=:pid) " +
					convertStateToQuery(context.getStates(), "g", "and"));
			query.setLong("pid", context.getPersonality().getId());
			return query;
		} else {
			throw new IllegalArgumentException("Invalid group context: " + context);
		}
	}

	static Query searchDivisions(Session session, TourneyDivision.Context context, boolean count) {
		final Query query = session.createQuery((count ? "select count(*) " : "") +
				"from HibernateTourneyDivision d " +
				"where d.tourney.number=:tourney " +
				(context.getLanguage() != null ? " and d.language=:language " : "") +
				(context.getSection() != null ? " and d.section=:section " : "") +
				convertStateToQuery(context.getStates(), "d", "and"));
		query.setInteger("tourney", context.getTourneyId().getNumber());
		if (context.getLanguage() != null) {
			query.setParameter("language", context.getLanguage());
		}
		if (context.getSection() != null) {
			query.setParameter("section", context.getSection());
		}
		return query;
	}

	static Query searchRegistrationRecords(Session session, RegistrationRecord.Context context, boolean count) {
		final Language language = context.getLanguage();
		final Query query = session.createQuery((count ? "select count(*) " : "") +
				"from HibernateRegistrationRecord as a where a.id.tourney=:tourney " +
				(language != null ? " and a.language=:language" : ""));
		query.setInteger("tourney", context.getTourney());
		if (language != null) {
			query.setParameter("language", language);
		}
		return query;
	}

	static Query searchUnregisteredPlayers(Session session, Tourney tourney) {
		Query namedQuery = session.createSQLQuery("select a.id as player " +
				"from account_personality as a left join tourney_regular_subs as s " +
				"on a.id=s.player where s.tourneyNumber!=:tourney or s.tourneyNumber is null " +
				"order by a.id asc");
		namedQuery.setParameter("tourney", tourney.getNumber());
		return namedQuery;
	}


	static RegistrationsSummaryImpl getRegistrationsSummary(Session session, int tourney, int round) {
		final Query query = session.createQuery("select language, section, count(id.player) from HibernateRegistrationRecord where id.tourney=:tid and id.round=:rid group by language, section");
		query.setInteger("tid", tourney);
		query.setInteger("rid", round);
		final RegistrationsSummaryImpl res = new RegistrationsSummaryImpl(tourney);
		final List list = query.list();
		for (Object o : list) {
			Object[] cols = (Object[]) o;
			res.setPlayers((Language) cols[0], (TourneySection) cols[1], ((Number) cols[2]).intValue());
		}
		return res;
	}


	private static String convertStateToQuery(EnumSet<TourneyEntity.State> states, String name, String prefix) {
		if (states == null) {
			return "";
		}

		final StringBuilder res = new StringBuilder();
		if (states.contains(TourneyEntity.State.SCHEDULED)) {
			res.append("(").append(name).append(".startedDate is null and ").append(name).append(".finishedDate is null)");
		}
		if (states.contains(TourneyEntity.State.ACTIVE)) {
			if (res.length() != 0) {
				res.append(" or ");
			}
			res.append("(").append(name).append(".startedDate is not null and ").append(name).append(".finishedDate is null)");
		}
		if (states.contains(TourneyEntity.State.FINISHED)) {
			if (res.length() != 0) {
				res.append(" or ");
			}
			res.append("(").append(name).append(".finishedDate is not null)");
		}
		if (res.length() != 0) {
			res.insert(0, " (");
			res.insert(0, prefix);
			res.append(")");
		}
		return res.toString();
	}
}
