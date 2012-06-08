package wisematches.playground.search.entity;

import wisematches.playground.search.SearchFilter;
import wisematches.playground.search.SearchManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface EntitySearchManager<E, C, F extends SearchFilter> extends SearchManager<E, C, F> {
}
