package wisematches.playground.tournament.impl;

import wisematches.database.Orders;
import wisematches.database.Range;
import wisematches.personality.Personality;
import wisematches.playground.search.SearchFilter;
import wisematches.playground.tournament.TournamentEntity;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTournamentDao {
	public <T extends TournamentEntity<T, I, C>, I extends TournamentEntity.Id<T, I, C>, C extends TournamentEntity.Context<T, I, C>> T getTournamentEntity(TournamentEntity.Id<T, I, C> id) {
		return null;
	}

	public <T extends TournamentEntity<T, I, C>, I extends TournamentEntity.Id<T, I, C>, C extends TournamentEntity.Context<T, I, C>> List<T> searchTournamentEntities(Personality person, TournamentEntity.Context<T, I, C> context, SearchFilter filter, Orders orders, Range range) {
		return null;
	}
}
