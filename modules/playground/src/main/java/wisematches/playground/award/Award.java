package wisematches.playground.award;

import wisematches.playground.GameRelationship;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface Award {
	String getCode();

	Date getAwardedDate();

	AwardWeight getWeight();

	GameRelationship getRelationship();
}