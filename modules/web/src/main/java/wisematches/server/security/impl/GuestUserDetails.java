package wisematches.server.security.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import wisematches.server.personality.Personality;
import wisematches.server.personality.player.computer.guest.GuestPlayer;
import wisematches.server.security.WMAuthorities;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GuestUserDetails extends Personality implements UserDetails {
	private static final Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>(EnumSet.of(WMAuthorities.USER, WMAuthorities.GUEST));

	public GuestUserDetails() {
		super(GuestPlayer.GUEST.getId());
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getUsername() {
		return GuestPlayer.GUEST.getNickname();
	}

	@Override
	public String getPassword() {
		return "guest";
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
