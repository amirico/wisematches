package wisematches.playground.search.descriptive;

import wisematches.playground.search.SearchFilter;
import wisematches.playground.search.SearchManager;

/**
 * The {@code DescriptiveSearchManager} extends base {@code SearchManager} and
 * processed entities described by {@code SearchableBean} and {@code SearchableProperty}
 * annotations.
 * <p/>
 * The {@code DescriptiveSearchManager} allows get {@code SearchableDescriptor} for
 * supported entity and can be used for dynamic entities parsing or analyzing.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface DescriptiveSearchManager<E, C, F extends SearchFilter> extends SearchManager<E, C, F> {
	/**
	 * Returns descriptor of entity.
	 *
	 * @return the entity descriptor.
	 */
	SearchableDescriptor getEntityDescriptor();
}
