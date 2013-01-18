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
import wisematches.core.personality.Player;
import wisematches.core.personality.PlayerManager;
import wisematches.core.personality.member.account.*;
import wisematches.server.security.AccountSecurityService;

import java.util.Collection;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WMUserDetailsService implements UserDetailsService, AccountSecurityService {
	private SaltSource saltSource;
	private PasswordEncoder passwordEncoder;

	private String administratorPassword;

	private PlayerManager playerManager;
	private AccountManager accountManager;
	private AccountLockManager accountLockManager;
	private AuthenticationManager authenticationManager;

	private final TheUserDetailsService userDetailsService = new TheUserDetailsService();


	protected final Log log = LogFactory.getLog(getClass());

	public WMUserDetailsService() {
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		if ("admin".equals(username)) {
			return new WMAdminDetails(administratorPassword);
		}

		final Account p = accountManager.findByEmail(username);
		if (p == null) {
			throw new UsernameNotFoundException("Account with email " + username + " not found in the system");
		}
		Player player = playerManager.getPlayer(p);
		if (player == null) {
			throw new UsernameNotFoundException("Player for account " + p + " can't be created");
		}
		final boolean locked = accountLockManager.isAccountLocked(p);
		return new WMPlayerDetails(player, p.getEmail(), p.getPassword(), !locked);
	}

	public void authenticatePlayer(Account player, String password) {
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		if (currentUser == null || !(currentUser instanceof UsernamePasswordAuthenticationToken)) {
			// This would indicate bad coding somewhere
			log.warn("Can't change password as no Authentication object found in context  or current user.");
			return;
		}

		// If an authentication manager has been set, re-authenticate the user with the supplied password.
		if (authenticationManager != null) {
			final Object credential;
			final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) currentUser;
			if (password == null) {
				credential = token.getCredentials();
			} else {
				credential = password;
			}

			final Authentication newAuthentication = createNewAuthentication(player, credential);
			if (log.isDebugEnabled()) {
				log.debug("Authenticating player '" + player + "' by code request");
			}
//			authenticationManager.authenticate(newAuthentication);
			SecurityContextHolder.getContext().setAuthentication(newAuthentication);
		} else {
			if (log.isDebugEnabled()) {
				log.debug("No authentication manager set. Password won't be re-checked.");
			}
		}
	}

	@Override
	public String encodePlayerPassword(Account player, String password) {
		return passwordEncoder.encodePassword(password, saltSource.getSalt(new SaltUserDetails(player)));
	}

	protected Authentication createNewAuthentication(Account player, Object credential) {
		final UserDetails user = loadUserByUsername(player.getEmail());

		return new UsernamePasswordAuthenticationToken(user, credential, user.getAuthorities());
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

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public void setSaltSource(SaltSource saltSource) {
		this.saltSource = saltSource;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public void setAdministratorPassword(String administratorPassword) {
		this.administratorPassword = administratorPassword;
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

	public static class SaltUserDetails implements UserDetails {
		private final Account account;

		public SaltUserDetails(Account account) {
			this.account = account;
		}

		@Override
		public Collection<GrantedAuthority> getAuthorities() {
			return null;
		}

		@Override
		public String getPassword() {
			return account.getPassword();
		}

		public String getNickname() {
			return account.getNickname();
		}

		@Override
		public String getUsername() {
			return account.getEmail();
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
}
