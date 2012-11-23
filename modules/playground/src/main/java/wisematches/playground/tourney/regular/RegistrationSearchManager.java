package wisematches.playground.tourney.regular;

import wisematches.database.Range;
import wisematches.playground.search.SearchFilter;
import wisematches.playground.search.SearchManager;

/**
 * {@code RegistrationSearchManager} provides ability to get information about registered/unregistered
 * player.
 * <p/>
 * The {@code SearchManager} provides ability to search only {@code RegistrationRecord}s in common way.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface RegistrationSearchManager extends SearchManager<RegistrationRecord, RegistrationRecord.Context, SearchFilter.NoFilter> {
	long[] searchUnregisteredPlayers(Tourney tourney, Range range);
}
