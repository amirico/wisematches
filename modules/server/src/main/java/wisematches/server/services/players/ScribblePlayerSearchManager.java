package wisematches.server.services.players;

import wisematches.core.search.SearchFilter;
import wisematches.core.search.descriptive.AbstractDescriptiveSearchManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribblePlayerSearchManager extends AbstractDescriptiveSearchManager<PlayerEntityBean, PlayerSearchArea, SearchFilter> {
	public ScribblePlayerSearchManager() {
		super(PlayerEntityBean.class);
	}

	@Override
	protected String getEntitiesList(PlayerSearchArea area, SearchFilter filter) {
		String r = "HibernateAccountImpl account, ScribbleStatisticsEditor stats";

		switch (area) {
			case PLAYERS:
				return r;
			case FRIENDS:
				return r + ", HibernateFriendRelation r";
			case FORMERLY:
				return r + ", ScribbleBoard board left join board.hands l left join board.hands r";
			default:
				throw new UnsupportedOperationException("Area filter not implemented for " + area);
		}
	}

	@Override
	protected String getWhereCriterias(PlayerSearchArea area, SearchFilter filter) {
		String r = "account.id=stats.playerId and ";
		switch (area) {
			case PLAYERS:
				return r + "not account.id=:pid";
			case FRIENDS:
				return r + "r.friend=account.id and r.person=:pid";
			case FORMERLY:
				return r + "r.playerId=account.id and not r.playerId=l.playerId and l.playerId=:pid";
			default:
				throw new UnsupportedOperationException("Area filter not implemented for " + area);
		}
	}

	@Override
	protected String getGroupCriterias(PlayerSearchArea context, SearchFilter filter) {
		return null;
	}
}
