package wisematches.server.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import wisematches.server.personality.account.Membership;

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

	private static final Map<Membership, Set<GrantedAuthority>> authoritiesCache = new HashMap<Membership, Set<GrantedAuthority>>();

	static {
		authoritiesCache.put(Membership.BASIC, new HashSet<GrantedAuthority>(EnumSet.of(USER, MEMBER)));
		authoritiesCache.put(Membership.GOLD, new HashSet<GrantedAuthority>(EnumSet.of(USER, MEMBER, GOLD)));
		authoritiesCache.put(Membership.SILVER, new HashSet<GrantedAuthority>(EnumSet.of(USER, MEMBER, SILVER)));
		authoritiesCache.put(Membership.PLATINUM, new HashSet<GrantedAuthority>(EnumSet.of(USER, MEMBER, PLATINUM)));
	}

	@Override
	public String getAuthority() {
		return name().toLowerCase();
	}

	/**
	 * Checks is this authority granted.
	 * <p/>
	 * This method uses {@code SecurityContextHolder#getContext()} to get a {@code Authentication}
	 * object and check the authorities.
	 *
	 * @return {@code true} if the authority granted; {@code false} - otherwise.
	 * @see SecurityContext
	 * @see SecurityContextHolder#getContext()
	 */
	public boolean isAuthorityGranted() {
		final SecurityContext context = SecurityContextHolder.getContext();
		if (context != null) {
			final Authentication authentication = context.getAuthentication();
			if (authentication != null && authentication.isAuthenticated()) {
				return authentication.getAuthorities().contains(this);
			}
		}
		return false;
	}

	public static Collection<GrantedAuthority> forMembership(Membership membership) {
		final Set<GrantedAuthority> authorities = authoritiesCache.get(membership);
		if (authorities == null) {
			return Collections.emptySet();
		}
		return authorities;
	}
}