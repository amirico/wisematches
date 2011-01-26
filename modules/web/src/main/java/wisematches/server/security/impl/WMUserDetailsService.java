package wisematches.server.security.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import wisematches.server.player.AccountManager;
import wisematches.server.player.Player;
import wisematches.server.player.PlayerListener;
import wisematches.server.player.lock.LockAccountListener;
import wisematches.server.player.lock.LockAccountManager;
import wisematches.server.security.PlayerSecurityService;

import java.util.Collections;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WMUserDetailsService implements UserDetailsService, PlayerSecurityService {
	private SaltSource saltSource;
	private PasswordEncoder passwordEncoder;

	private AccountManager accountManager;
	private LockAccountManager lockAccountManager;
	private AuthenticationManager authenticationManager;

	protected final Log log = LogFactory.getLog(getClass());

	private final TheUserDetailsService externalListeners = new TheUserDetailsService();

	public WMUserDetailsService() {
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		final Player p = accountManager.findByEmail(username);
		if (p == null) {
			throw new UsernameNotFoundException("User with email " + username + " not found in the system");
		}
		final boolean locked = lockAccountManager.isAccountLocked(p);
		return new WMUserDetails(p, !locked, WMAuthorities.forMembership(p.getMembership()));
	}

	public void authenticatePlayer(Player player, String password) {
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		if (currentUser == null) {
			// This would indicate bad coding somewhere
			log.warn("Can't change password as no Authentication object found in context  or current user.");
			return;
		}

		String username = currentUser.getName();
		// If an authentication manager has been set, re-authenticate the user with the supplied password.
		if (authenticationManager != null) {
			final Authentication newAuthentication = createNewAuthentication(player);
			if (log.isDebugEnabled()) {
				log.debug("Authenticating player '" + player + "' by code request");
			}
			authenticationManager.authenticate(newAuthentication);
			SecurityContextHolder.getContext().setAuthentication(newAuthentication);
		} else {
			if (log.isDebugEnabled()) {
				log.debug("No authentication manager set. Password won't be re-checked.");
			}
		}
	}

	@Override
	public String encodePlayerPassword(Player player, String password) {
		final WMUserDetails details = new WMUserDetails(player, false, Collections.<GrantedAuthority>emptySet());
		return passwordEncoder.encodePassword(password, saltSource.getSalt(details));
	}

	protected Authentication createNewAuthentication(Player player) {
		final UserDetails user = loadUserByUsername(player.getEmail());

		final UsernamePasswordAuthenticationToken newAuthentication =
				new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
		newAuthentication.setDetails(user);

		return newAuthentication;
	}

	public void setAccountManager(AccountManager accountManager) {
		if (this.accountManager != null) {
			this.accountManager.removePlayerListener(externalListeners);
		}

		this.accountManager = accountManager;

		if (this.accountManager != null) {
			this.accountManager.addPlayerListener(externalListeners);
		}
	}

	public void setLockAccountManager(LockAccountManager lockAccountManager) {
		if (this.lockAccountManager != null) {
			this.lockAccountManager.removeLockAccountListener(externalListeners);
		}

		this.lockAccountManager = lockAccountManager;

		if (this.lockAccountManager != null) {
			this.lockAccountManager.addLockAccountListener(externalListeners);
		}
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public void setSaltSource(SaltSource saltSource) {
		this.saltSource = saltSource;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	private class TheUserDetailsService implements PlayerListener, LockAccountListener {
		@Override
		public void playerUpdated(Player oldInfo, Player newInfo) {
//			authenticatePlayer(newInfo);
		}

		@Override
		public void accountLocked(Player player, String publicReason, String privateReason, Date unlockdate) {
//			authenticatePlayer(player);
		}

		@Override
		public void accountUnlocked(Player player) {
//			authenticatePlayer(player);
		}
	}
}
