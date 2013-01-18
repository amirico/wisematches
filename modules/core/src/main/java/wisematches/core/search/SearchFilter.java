package wisematches.core.search;

/**
 * Base interface for search criteria.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface SearchFilter {
	final class NoFilter implements SearchFilter {
		private NoFilter() {
		}
	}
}