package wisematches.core.security.userdetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import wisematches.core.Personality;
import wisematches.core.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

	public PersonalityDetails(Personality personality, String username, String password, boolean locked, boolean expired, Collection<String> authorities) {
		this.username = username;
		this.password = password;
		this.accountLocked = locked;
		this.accountExpired = expired;
		this.personality = personality;

		final Set<GrantedAuthority> a = new HashSet<>();
		if (authorities != null) {
			for (String authority : authorities) {
				a.add(new SimpleGrantedAuthority(authority));
			}
		}
		this.authorities = Collections.unmodifiableSet(a);
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
		if (personality instanceof Player) {
			Player player = (Player) personality;
			return player.getNickname();
		}
		return "NO_NICKNAME";
	}

	public Personality getPersonality() {
		return personality;
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
