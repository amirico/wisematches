package wisematches.playground;

import wisematches.core.search.SearchFilter;
import wisematches.core.search.SearchManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameSearchManager<S extends GameSettings, D extends BoardDescription<S, ?>> extends SearchManager<D, Void, SearchFilter.NoFilter> {
}
