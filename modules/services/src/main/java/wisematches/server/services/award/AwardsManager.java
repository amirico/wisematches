package wisematches.server.services.award;

import wisematches.core.Player;
import wisematches.core.search.SearchManager;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface AwardsManager extends SearchManager<Award, AwardContext> {
	void addAwardsListener(AwardsListener l);

	void removeAwardsListener(AwardsListener l);


	AwardsSummary getAwardsSummary(Player player);


	AwardDescriptor getAwardDescriptor(int code);

	AwardDescriptor getAwardDescriptor(String name);


	Collection<AwardDescriptor> getAwardDescriptors();
}