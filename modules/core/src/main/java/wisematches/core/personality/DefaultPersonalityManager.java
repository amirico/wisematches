package wisematches.core.personality;

import org.springframework.beans.factory.InitializingBean;
import wisematches.core.*;
import wisematches.core.personality.player.account.Account;
import wisematches.core.personality.player.account.AccountListener;
import wisematches.core.personality.player.account.AccountManager;
import wisematches.core.personality.player.membership.MembershipCard;
import wisematches.core.personality.player.membership.MembershipListener;
import wisematches.core.personality.player.membership.MembershipManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultPersonalityManager implements PersonalityManager, InitializingBean {
	private AccountManager accountManager;
	private MembershipManager membershipManager;


	private final Map<RobotType, Robot> robotMap = new HashMap<>();
	private final Map<Language, Visitor> visitorMap = new HashMap<>();

	// TODO: too many objects in cache
	private final Map<Long, Personality> playerMap = new HashMap<>();

	private final TheMemberPlayerListener memberPlayerListener = new TheMemberPlayerListener();
	private final Collection<PersonalityListener> listeners = new CopyOnWriteArraySet<>();

	public DefaultPersonalityManager() {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (RobotType type : RobotType.values()) {
			final DefaultRobot value = new DefaultRobot(type);
			robotMap.put(type, value);
			playerMap.put(value.getId(), value);
		}

		for (Language language : Language.values()) {
			final Visitor visitor = new DefaultVisitor(language);
			visitorMap.put(language, visitor);
			playerMap.put(visitor.getId(), visitor);
		}
	}

	@Override
	public void addPersonalityListener(PersonalityListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	@Override
	public void removePersonalityListener(PersonalityListener listener) {
		listeners.remove(listener);
	}

	@Override
	public Personality getPerson(long id) {
		if (id < 1000) {
			return playerMap.get(id);
		} else {
			return getPlayer(id);
		}
	}

	@Override
	public Player getPlayer(long id) {
		if (id < 1000) {
			return null;
		}
		final Personality personality = playerMap.get(id);
		if (personality == null) {
			final Account account = accountManager.getAccount(id);
			if (account != null) {
				final DefaultPlayer memberPlayer = createMemberPlayer(account);
				playerMap.put(memberPlayer.getId(), memberPlayer);
				return memberPlayer;
			}
		}
		return (Player) personality;
	}

	@Override
	public Robot getRobot(RobotType type) {
		return robotMap.get(type);
	}

	@Override
	public Visitor getVisitor(Language language) {
		return visitorMap.get(language);
	}

	public void setAccountManager(AccountManager accountManager) {
		if (this.accountManager != null) {
			this.accountManager.removeAccountListener(memberPlayerListener);
		}

		this.accountManager = accountManager;

		if (this.accountManager != null) {
			this.accountManager.addAccountListener(memberPlayerListener);
		}
	}

	public void setMembershipManager(MembershipManager membershipManager) {
		if (this.membershipManager != null) {
			this.membershipManager.removeMembershipListener(memberPlayerListener);
		}

		this.membershipManager = membershipManager;

		if (this.membershipManager != null) {
			this.membershipManager.addMembershipListener(memberPlayerListener);
		}
	}

	private DefaultPlayer createMemberPlayer(Account account) {
		final PlayerType card = membershipManager.getMembership(account);
		return new DefaultPlayer(account, card);
	}

	private final class TheMemberPlayerListener implements AccountListener, MembershipListener {
		private TheMemberPlayerListener() {
		}

		@Override
		public void accountCreated(Account account) {
			for (PersonalityListener listener : listeners) {
				listener.playerRegistered(createMemberPlayer(account));
			}
		}

		@Override
		public void accountRemove(Account account) {
			Player remove = (Player) playerMap.remove(account.getId());
			if (remove == null) {
				remove = createMemberPlayer(account);
			}
			for (PersonalityListener listener : listeners) {
				listener.playerUnregistered(remove);
			}
		}

		@Override
		public void accountUpdated(Account oldAccount, Account newAccount) {
			playerMap.put(newAccount.getId(), createMemberPlayer(newAccount));
		}

		@Override
		public void membershipCardUpdated(Account account, MembershipCard oldCard, MembershipCard newCard) {
			playerMap.put(account.getId(), new DefaultPlayer(account, newCard.getValidMembership()));
		}
	}
}
