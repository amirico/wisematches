package wisematches.server.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import wisematches.core.personality.Player;
import wisematches.core.personality.PlayerType;
import wisematches.core.personality.member.Membership;

import java.util.*;

/**
 * TODO: validate after refactoring
 *
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

	static final Set<GrantedAuthority> GUEST_AUTHORITIES = new HashSet<GrantedAuthority>(EnumSet.of(USER, GUEST));

	private static final Map<Membership, Set<GrantedAuthority>> authoritiesCache = new HashMap<>();

	static {
		authoritiesCache.put(Membership.DEFAULT, new HashSet<GrantedAuthority>(EnumSet.of(USER, MEMBER)));
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

	public static Collection<GrantedAuthority> forPlayer(Player player) {
		final PlayerType playerType = player.getPlayerType();
		if (playerType.isGuest()) {
			return GUEST_AUTHORITIES;
		}
		if (player.getMembership() != null) {
			return authoritiesCache.get(player.getMembership());
		}
		return Collections.emptyList();
	}
}