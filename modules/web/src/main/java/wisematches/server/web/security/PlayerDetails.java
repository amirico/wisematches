package wisematches.server.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.security.SocialUserDetails;
import wisematches.core.Member;
import wisematches.core.Player;
import wisematches.core.secure.PlayerContainer;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class PlayerDetails implements UserDetails, SocialUserDetails, PlayerContainer {
	private final String username;
	private final String password;

	private final boolean accountLocked;
	private final boolean accountExpired;

	private final Player player;
	private final Collection<GrantedAuthority> authorities;

	public PlayerDetails(Player player, String username, String password, boolean locked, boolean expired, Collection<String> authorities) {
		this.username = username;
		this.password = password;
		this.accountLocked = locked;
		this.accountExpired = expired;
		this.player = player;

		final Set<GrantedAuthority> a = new HashSet<>();
		if (authorities != null) {
			for (String authority : authorities) {
				a.add(new SimpleGrantedAuthority(authority));
			}
		}
		this.authorities = Collections.unmodifiableSet(a);
	}

	@Override
	public String getUserId() {
		return String.valueOf(player.getId());
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}


	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getNickname() {
		if (player instanceof Member) {
			return ((Member) player).getNickname();
		}
		return "NO_NICKNAME";
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !accountLocked;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !accountExpired;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
}
