package wisematches.core.security.userdetails;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import wisematches.core.Language;
import wisematches.core.PersonalityManager;
import wisematches.core.Player;
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
public class PersonalityDetailsService implements UserDetailsService {
	private AccountManager accountManager;
	private AccountLockManager accountLockManager;
	private AccountRecoveryManager accountRecoveryManager;

	private PersonalityManager personalityManager;

	private final Set<String> administrators = new HashSet<>();

	public PersonalityDetailsService() {
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return loadPlayerByEmail(username);
	}

	public PersonalityDetails loadPlayerByEmail(String email) throws UsernameNotFoundException {
		final Account account = accountManager.findByEmail(email);
		if (account == null) {
			throw new UsernameNotFoundException("Account with email " + email + " not found in the system");
		}

		final Player player = personalityManager.getPlayer(account.getId());
		if (player == null) {
			throw new UsernameNotFoundException("Player for account " + account + " can't be created");
		}
		final boolean locked = accountLockManager.isAccountLocked(account);
		final boolean expired = (accountRecoveryManager.getToken(account) != null);

		final Collection<String> authorities = new HashSet<>();
		if (administrators.contains(account.getNickname())) {
			authorities.add("admin");
		}
		authorities.add("player");
		authorities.add(player.getPlayerType().name().toLowerCase());
		return new PersonalityDetails(player, account.getEmail(), null, locked, expired, authorities);
	}

	public PersonalityDetails loadVisitorByLanguage(Language language) throws UsernameNotFoundException {
		if (language == null) {
			throw new UsernameNotFoundException("Unsupported guest language: " + language);
		}

		final Visitor visitor = personalityManager.getVisitor(language);
		if (visitor == null) {
			throw new UsernameNotFoundException("Unsupported guest language: " + language);
		}
		return new PersonalityDetails(visitor, "guest", null, false, false, Arrays.asList("player", "guest"));
	}

	public boolean isPasswordValid(PersonalityDetails details, String rawPass) {
		return accountManager.checkAccountCredentials(details.getPersonality().getId(), rawPass);
	}

	public void setAdministrators(Set<String> administrators) {
		this.administrators.clear();

		if (administrators != null) {
			this.administrators.addAll(administrators);
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