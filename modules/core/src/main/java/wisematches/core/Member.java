package wisematches.core;

import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class Member extends Player {
	protected Member(long id) {
		super(id);
	}

	public abstract String getEmail();

	public abstract String getNickname();

	public abstract TimeZone getTimeZone();

	public abstract Membership getMembership();

	@Override
	public final PersonalityType getType() {
		return PersonalityType.MEMBER;
	}
}
