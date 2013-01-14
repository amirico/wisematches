package wisematches.playground.award;

import wisematches.personality.Personality;
import wisematches.playground.search.SearchFilter;
import wisematches.playground.search.SearchManager;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface AwardsManager extends SearchManager<Award, AwardContext, SearchFilter.NoFilter> {
	void addAwardsListener(AwardsListener l);

	void removeAwardsListener(AwardsListener l);


	AwardDescriptor getAwardDescriptor(String code);

	Collection<AwardDescriptor> getAwardDescriptors();


	AwardsSummary getAwardsSummary(Personality personality);
}