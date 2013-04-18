package wisematches.server.services.relations.players.impl;

import wisematches.core.search.Range;
import wisematches.core.search.descriptive.AbstractDescriptiveSearchManager;
import wisematches.server.services.relations.players.PlayerContext;
import wisematches.server.services.relations.players.PlayerEntityBean;
import wisematches.server.services.relations.players.PlayerRelationship;
import wisematches.server.services.relations.players.PlayerSearchManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernatePlayerSearchManager extends AbstractDescriptiveSearchManager<PlayerEntityBean, PlayerContext> implements PlayerSearchManager {
	public HibernatePlayerSearchManager() {
		super(PlayerEntityBean.class);
	}

	@Override
	protected String getEntitiesList(PlayerContext context) {
		String r = "HibernateAccount account, ScribbleStatisticsEditor stats";

		final PlayerRelationship relationship = context.getRelationship();
		if (relationship == null) {
			return r;
		}

		switch (relationship) {
			case FRIENDS:
				return r + ", HibernateFriendRelation r";
			case FORMERLY:
				return r + ", ScribbleBoard board left join board.hands l left join board.hands r";
			default:
				throw new UnsupportedOperationException("Area filter not implemented for " + relationship);
		}
	}

	@Override
	protected String getWhereCriterias(PlayerContext context) {
		StringBuilder r = new StringBuilder("account.id=stats.playerId and ");

		final String nickname = context.getNickname();
		if (nickname != null && !nickname.trim().isEmpty()) {
			r.append("account.nickname like '%").append(nickname).append("%' and ");
		}

		final Range rating = context.getRating();
		if (rating != null) {
			if (rating.getFirstResult() > 0) {
				r.append("stats.rating>=").append(rating.getFirstResult()).append(" and ");
			}
			if (rating.getMaxResults() > 0) {
				r.append("stats.rating<=").append(rating.getFirstResult() + rating.getMaxResults()).append(" and ");
			}
		}

		final Range activeGames = context.getActiveGames();
		if (activeGames != null) {
			if (activeGames.getFirstResult() > 0) {
				r.append("stats.activeGames>=").append(activeGames.getFirstResult()).append(" and ");
			}
			if (activeGames.getMaxResults() > 0) {
				r.append("stats.activeGames<=").append(activeGames.getFirstResult() + activeGames.getMaxResults()).append(" and ");
			}
		}

		final Range finishedGames = context.getFinishedGames();
		if (finishedGames != null) {
			if (finishedGames.getFirstResult() > 0) {
				r.append("stats.finishedGames>=").append(finishedGames.getFirstResult()).append(" and ");
			}
			if (finishedGames.getMaxResults() > 0) {
				r.append("stats.finishedGames<=").append(finishedGames.getFirstResult() + finishedGames.getMaxResults()).append(" and ");
			}
		}

		if (!Float.isNaN(context.getMaxAverageMoveTime())) {
			r.append("stats.averageMoveTime != 0 and stats.averageMoveTime <= ").append(context.getMaxAverageMoveTime()).append(" and ");
		}

		if (context.getLastMoveTime() != null) {
			r.append("stats.lastMoveTime>=:lmt and ");
		}

		final PlayerRelationship relationship = context.getRelationship();
		if (relationship == null) {
			return r.append("not account.id=:pid").toString();
		}

		switch (relationship) {
			case FRIENDS:
				return r.append("r.friend=account.id and r.person=:pid").toString();
			case FORMERLY:
				return r.append("r.playerId=account.id and not r.playerId=l.playerId and l.playerId=:pid").toString();
			default:
				throw new UnsupportedOperationException("Area filter not implemented for " + relationship);
		}
	}

	@Override
	protected Object getNamedParameter(PlayerContext context, String name) {
		if (name.equals("lmt")) {
			return context.getLastMoveTime();
		}
		return null;
	}

	@Override
	protected String getGroupCriterias(PlayerContext context) {
		return null;
	}
}
