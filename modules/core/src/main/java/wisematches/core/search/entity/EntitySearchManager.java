package wisematches.core.search.entity;

import wisematches.core.search.SearchFilter;
import wisematches.core.search.SearchManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface EntitySearchManager<E, C, F extends SearchFilter> extends SearchManager<E, C, F> {
}
