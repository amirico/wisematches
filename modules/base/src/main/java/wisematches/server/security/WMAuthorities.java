package wisematches.server.security;

import org.springframework.security.core.GrantedAuthority;
import wisematches.server.player.Membership;

import java.util.Collection;
import java.util.EnumSet;

/**
 * @author klimese
 */
public enum WMAuthorities implements GrantedAuthority {
	ADMIN,
	USER,
	ANONYMOUS,
	MEMBER,
	GUEST,
	SILVER,
	GOLD,
	PLATINUM;

	@Override
	public String getAuthority() {
		return name().toLowerCase();
	}

	public static Collection<? extends GrantedAuthority> forMembership(Membership membership) {
		switch (membership) {
			case GUEST:
				return EnumSet.of(USER, GUEST);
			case BASIC:
				return EnumSet.of(USER, MEMBER);
			case SILVER:
				return EnumSet.of(USER, MEMBER, SILVER);
			case GOLD:
				return EnumSet.of(USER, MEMBER, GOLD);
			case PLATINUM:
				return EnumSet.of(USER, MEMBER, PLATINUM);
		}
		return EnumSet.of(ANONYMOUS);
	}
}