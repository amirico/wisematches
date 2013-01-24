package wisematches.server.security.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import wisematches.core.Personality;
import wisematches.core.personality.player.MemberPlayer;
import wisematches.server.security.WMAuthorities;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WMPlayerDetails implements UserDetails {
	private final MemberPlayer player;
	private final String username;
	private final String password;
	private final boolean accountNonLocked;
	private final Collection<GrantedAuthority> authorities;

	public WMPlayerDetails(MemberPlayer player, String username, String password, boolean accountNonLocked) {
		this.player = player;
		this.username = username;
		this.password = password;
		this.accountNonLocked = accountNonLocked;
		authorities = WMAuthorities.forPlayer(player);
	}

	public Personality getPlayer() {
		return player;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public String getNickname() {
		return player.getNickname();
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
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
