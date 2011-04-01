package wisematches.server.security.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import wisematches.server.personality.account.*;
import wisematches.server.security.PlayerSecurityService;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WMUserDetailsService implements UserDetailsService, PlayerSecurityService {
	private SaltSource saltSource;
	private PasswordEncoder passwordEncoder;

	private AccountManager accountManager;
	private AccountLockManager accountLockManager;
	private AuthenticationManager authenticationManager;

	private final TheUserDetailsService userDetailsService = new TheUserDetailsService();

	protected final Log log = LogFactory.getLog(getClass());

	public WMUserDetailsService() {
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		final Account p = accountManager.findByEmail(username);
		if (p == null) {
			throw new UsernameNotFoundException("User with email " + username + " not found in the system");
		}
		final boolean locked = accountLockManager.isAccountLocked(p);
		return new MemberUserDetails(p, !locked);
	}

	public void authenticatePlayer(Account player, String password) {
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
	public String encodePlayerPassword(Account player, String password) {
		return passwordEncoder.encodePassword(password, saltSource.getSalt(new MemberUserDetails(player, false)));
	}

	protected Authentication createNewAuthentication(Account player) {
		final UserDetails user = loadUserByUsername(player.getEmail());

		final UsernamePasswordAuthenticationToken newAuthentication =
				new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
		newAuthentication.setDetails(user);
		return newAuthentication;
	}

	public void setAccountManager(AccountManager accountManager) {
		if (this.accountManager != null) {
			this.accountManager.removeAccountListener(userDetailsService);
		}

		this.accountManager = accountManager;

		if (this.accountManager != null) {
			this.accountManager.addAccountListener(userDetailsService);
		}
	}

	public void setAccountLockManager(AccountLockManager accountLockManager) {
		if (this.accountLockManager != null) {
			this.accountLockManager.removeAccountLockListener(userDetailsService);
		}

		this.accountLockManager = accountLockManager;

		if (this.accountLockManager != null) {
			this.accountLockManager.addAccountLockListener(userDetailsService);
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

	private class TheUserDetailsService implements AccountListener, AccountLockListener {
		private TheUserDetailsService() {
		}

		@Override
		public void accountCreated(Account account) {
		}

		@Override
		public void accountRemove(Account account) {
		}

		@Override
		public void accountUpdated(Account oldAccount, Account newAccount) {
//			authenticatePlayer(newInfo);
		}

		@Override
		public void accountLocked(Account player, String publicReason, String privateReason, Date unlockdate) {
//			authenticatePlayer(player);
		}

		@Override
		public void accountUnlocked(Account player) {
//			authenticatePlayer(player);
		}
	}
}
