package wisematches.server.services.award;

import wisematches.playground.GameRelationship;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface Award {
	long getRecipient();

	Date getAwardedDate();

	AwardWeight getWeight();

	AwardDescriptor getDescriptor();

	GameRelationship getRelationship();
}