package wisematches.server.security.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import wisematches.server.personality.Personality;
import wisematches.server.personality.account.Account;
import wisematches.server.security.WMAuthorities;

import java.util.Collection;

/**
 * This is implementation of Spring Security {@code UserDetails} interface that extends {@code Player} interface as
 * well. So any authorized user is a player by default.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MemberUserDetails extends Personality implements UserDetails {
	private final Account account;
	private final boolean accountNonLocked;
	private final Collection<GrantedAuthority> authorities;

	public MemberUserDetails(Account account, boolean accountNonLocked) {
		this(account, WMAuthorities.forMembership(account.getMembership()), accountNonLocked);
	}

	public MemberUserDetails(Account account, Collection<GrantedAuthority> authorities, boolean accountNonLocked) {
		super(account.getId());
		this.account = account;
		this.authorities = authorities;
		this.accountNonLocked = accountNonLocked;
	}

	public String getNickname() {
		return account.getNickname();
	}

	@Override
	public String getUsername() {
		return account.getEmail();
	}

	@Override
	public String getPassword() {
		return account.getPassword();
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

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}
}
