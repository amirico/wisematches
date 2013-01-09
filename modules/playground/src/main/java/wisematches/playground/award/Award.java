package wisematches.playground.award;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class Award {
	private final String code;
	private final AwardType type;
//	private final Collection<AwardCondition> conditions;

	public Award(String code, AwardType type,
				 Collection<AwardCondition> conditions) {
		this.code = code;
		this.type = type;
	}

	public Award(String code, AwardType type,
				 Collection<AwardCondition> bronze,
				 Collection<AwardCondition> silver,
				 Collection<AwardCondition> gold) {
		this.code = code;
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public AwardType getType() {
		return type;
	}
}