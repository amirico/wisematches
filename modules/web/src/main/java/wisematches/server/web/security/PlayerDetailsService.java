package wisematches.server.web.security;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetailsService;
import wisematches.core.Language;
import wisematches.core.Member;
import wisematches.core.PersonalityManager;
import wisematches.core.Visitor;
import wisematches.core.personality.player.account.Account;
import wisematches.core.personality.player.account.AccountLockManager;
import wisematches.core.personality.player.account.AccountManager;
import wisematches.core.personality.player.account.AccountRecoveryManager;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerDetailsService implements UserDetailsService, SocialUserDetailsService {
	private AccountManager accountManager;
	private AccountLockManager accountLockManager;
	private AccountRecoveryManager accountRecoveryManager;

	private PersonalityManager personalityManager;

	private final Set<Long> moderators = new HashSet<>();
	private final Set<Long> administrators = new HashSet<>();

	public PlayerDetailsService() {
	}

	@Override
	public PlayerDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
		final Account account = accountManager.getAccount(Long.valueOf(userId));
		if (account == null) {
			throw new UsernameNotFoundException("Account with id " + userId + " not found in the system");
		}

		final Member member = personalityManager.getMember(account.getId());
		if (member == null) {
			throw new UsernameNotFoundException("Player for account " + account + " can't be created");
		}
		final boolean locked = accountLockManager.isAccountLocked(account);
		final boolean expired = (accountRecoveryManager.getToken(account) != null);

		final Collection<String> authorities = new HashSet<>();
		if (administrators.contains(account.getId())) {
			authorities.add("admin");
		}
		if (moderators.contains(account.getId())) {
			authorities.add("moderator");
		}
		authorities.add("player");
		authorities.add(member.getMembership().name().toLowerCase());
		return new PlayerDetails(member, account.getEmail(), null, locked, expired, authorities);
	}

	public PlayerDetails loadUserByAccount(Account account) {
		final Member member = personalityManager.getMember(account.getId());
		if (member == null) {
			throw new UsernameNotFoundException("Player for account " + account + " can't be created");
		}
		final boolean locked = accountLockManager.isAccountLocked(account);
		final boolean expired = (accountRecoveryManager.getToken(account) != null);

		final Collection<String> authorities = new HashSet<>();
		if (administrators.contains(account.getId())) {
			authorities.add("admin");
		}
		if (moderators.contains(account.getId())) {
			authorities.add("moderator");
		}
		authorities.add("player");
		authorities.add(member.getMembership().name().toLowerCase());
		return new PlayerDetails(member, account.getEmail(), null, locked, expired, authorities);
	}

	@Override
	public PlayerDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final Account account = accountManager.findByEmail(username);
		if (account == null) {
			throw new UsernameNotFoundException("Account with email " + username + " not found in the system");
		}
		return loadUserByAccount(account);
	}

	public PlayerDetails loadVisitorByLanguage(Language language) throws UsernameNotFoundException {
		if (language == null) {
			throw new UsernameNotFoundException("Null guest language");
		}

		final Visitor visitor = personalityManager.getVisitor(language);
		if (visitor == null) {
			throw new UsernameNotFoundException("Unsupported guest language: " + language);
		}
		return new PlayerDetails(visitor, "guest", null, false, false, Arrays.asList("player", "guest"));
	}

	public boolean isPasswordValid(PlayerDetails details, String rawPass) {
		return accountManager.checkAccountCredentials(details.getPlayer().getId(), rawPass);
	}

	public void setModerators(String moderators) {
		this.moderators.clear();

		if (moderators != null) {
			for (String moderator : moderators.split(",")) {
				this.moderators.add(Long.valueOf(moderator));
			}
		}
	}

	public void setAdministrators(String administrators) {
		this.administrators.clear();

		if (administrators != null) {
			for (String administrator : administrators.split(",")) {
				this.administrators.add(Long.valueOf(administrator));
			}
		}
	}

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	public void setAccountLockManager(AccountLockManager accountLockManager) {
		this.accountLockManager = accountLockManager;
	}

	public void setPersonalityManager(PersonalityManager personalityManager) {
		this.personalityManager = personalityManager;
	}

	public void setAccountRecoveryManager(AccountRecoveryManager accountRecoveryManager) {
		this.accountRecoveryManager = accountRecoveryManager;
	}
}
