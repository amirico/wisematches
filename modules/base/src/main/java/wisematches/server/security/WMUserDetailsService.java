package wisematches.server.security;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import wisematches.server.player.AccountManager;
import wisematches.server.player.Player;
import wisematches.server.player.locks.LockAccountManager;

/**
 * @author klimese
 */
public class WMUserDetailsService implements UserDetailsService {
	private AccountManager accountManager;
	private LockAccountManager lockAccountManager;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		final Player p = accountManager.findByEmail(username);
		if (p == null) {
			throw new UsernameNotFoundException("User with email " + username + " not found in the system");
		}
		final boolean locked = lockAccountManager.isAccountLocked(p);
		return new WMUserDetails(p, !locked, WMAuthorities.forMembership(p.getMembership()));
	}

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	public void setLockAccountManager(LockAccountManager lockAccountManager) {
		this.lockAccountManager = lockAccountManager;
	}
}
