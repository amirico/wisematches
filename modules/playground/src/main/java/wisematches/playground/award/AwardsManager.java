package wisematches.playground.award;

import wisematches.core.Player;
import wisematches.core.search.SearchFilter;
import wisematches.core.search.SearchManager;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface AwardsManager extends SearchManager<Award, AwardContext, SearchFilter.NoFilter> {
	void addAwardsListener(AwardsListener l);

	void removeAwardsListener(AwardsListener l);


	AwardsSummary getAwardsSummary(Player player);


	AwardDescriptor getAwardDescriptor(String code);

	Collection<AwardDescriptor> getAwardDescriptors();
}