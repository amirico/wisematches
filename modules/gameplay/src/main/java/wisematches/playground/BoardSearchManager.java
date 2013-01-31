package wisematches.playground;

import wisematches.core.search.SearchFilter;
import wisematches.core.search.SearchManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface BoardSearchManager<D extends BoardDescription, C extends BoardContext> extends SearchManager<D, C, SearchFilter.NoFilter> {
}
