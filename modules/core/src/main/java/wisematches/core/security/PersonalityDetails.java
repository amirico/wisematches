package wisematches.core.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import wisematches.core.Personality;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class PersonalityDetails implements UserDetails {
	private final String username;
	private final String password;

	private final boolean accountLocked;
	private final boolean accountExpired;

	private final Personality personality;
	private final Collection<GrantedAuthority> authorities;

	PersonalityDetails(Personality personality, String username, String password, boolean locked, boolean expired, Collection<String> authorities) {
		this.username = username;
		this.password = password;
		this.accountLocked = locked;
		this.accountExpired = expired;
		this.personality = personality;

		this.authorities = new HashSet<>();
		if (authorities != null) {
			for (String authority : authorities) {
				this.authorities.add(new SimpleGrantedAuthority(authority));
			}
		}
	}

	public Personality getPersonality() {
		return personality;
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
