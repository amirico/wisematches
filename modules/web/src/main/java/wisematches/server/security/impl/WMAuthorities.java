package wisematches.server.security.impl;

import org.springframework.security.core.GrantedAuthority;
import wisematches.server.player.Membership;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
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

	private static final Map<Membership, EnumSet<WMAuthorities>> authoritiesCache = new HashMap<Membership, EnumSet<WMAuthorities>>();

	static {
		authoritiesCache.put(Membership.GUEST, EnumSet.of(USER, GUEST));
		authoritiesCache.put(Membership.BASIC, EnumSet.of(USER, MEMBER));
		authoritiesCache.put(Membership.SILVER, EnumSet.of(USER, MEMBER, SILVER));
		authoritiesCache.put(Membership.GOLD, EnumSet.of(USER, MEMBER, GOLD));
		authoritiesCache.put(Membership.PLATINUM, EnumSet.of(USER, MEMBER, PLATINUM));
	}

	@Override
	public String getAuthority() {
		return name().toLowerCase();
	}

	public static Collection<? extends GrantedAuthority> forMembership(Membership membership) {
		final EnumSet<WMAuthorities> authorities = authoritiesCache.get(membership);
		if (authorities == null) {
			return Collections.emptySet();
		}
		return authorities;
	}
}