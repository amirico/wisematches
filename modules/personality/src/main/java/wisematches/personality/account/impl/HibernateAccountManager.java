package wisematches.personality.account.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.account.*;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateAccountManager implements AccountManager {
	private SessionFactory sessionFactory;
	private AccountLockManager accountLockManager;

	private final Collection<AccountListener> accountListeners = new CopyOnWriteArraySet<AccountListener>();
	private static final String CHECK_ACCOUNT_AVAILABILITY = "" +
			"select account.nickname, account.email " +
			"from wisematches.personality.account.impl.HibernateAccountImpl as account " +
			"where account.nickname = ? or account.email = ?";

	public HibernateAccountManager() {
	}

	@Override
	public void addAccountListener(AccountListener l) {
		if (l != null) {
			accountListeners.add(l);
		}
	}

	@Override
	public void removeAccountListener(AccountListener l) {
		if (l != null) {
			accountListeners.remove(l);
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_UNCOMMITTED)
	public Account getAccount(long playerId) {
		return (Account) sessionFactory.getCurrentSession().get(HibernateAccountImpl.class, playerId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_UNCOMMITTED)
	public Account findByEmail(String email) {
		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery("from wisematches.personality.account.impl.HibernateAccountImpl user where user.email=?");
		query.setParameter(0, email);
		final List l = query.list();
		if (l.size() != 1) {
			return null;
		}
		return (Account) l.get(0);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_UNCOMMITTED)
	public Account findByUsername(String username) {
		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery("from wisematches.personality.account.impl.HibernateAccountImpl as user where user.nickname=?");
		query.setParameter(0, username);
		final List l = query.list();
		if (l.size() != 1) {
			return null;
		}
		return (Account) l.get(0);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public Account createAccount(Account account) throws DuplicateAccountException, InadmissibleUsernameException {
		checkAccount(account);

		final Session session = sessionFactory.getCurrentSession();
		final HibernateAccountImpl hp = new HibernateAccountImpl(account);
		session.save(hp);

		for (AccountListener accountListener : accountListeners) {
			accountListener.accountCreated(hp);
		}
		return hp;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public Account updateAccount(Account account) throws UnknownAccountException, DuplicateAccountException, InadmissibleUsernameException {
		Account oldAccount = getAccount(account.getId());
		if (oldAccount == null) {
			throw new UnknownAccountException(account);
		}

		if (!oldAccount.getEmail().equals(account.getEmail())) {
			if (!checkAccountAvailable(account.getNickname(), account.getEmail()).isEmailAvailable()) {
				throw new DuplicateAccountException(account, "email");
			}
		}

		// Copy previous state
		oldAccount = new AccountEditor(oldAccount).createAccount();

		// merge and update
		final Session session = sessionFactory.getCurrentSession();
		final Account a = (Account) session.merge(new HibernateAccountImpl(account));
		for (AccountListener playerListener : accountListeners) {
			playerListener.accountUpdated(oldAccount, a);
		}
		return a;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void removeAccount(Account account) throws UnknownAccountException {
		final HibernateAccountImpl hp = (HibernateAccountImpl) getAccount(account.getId());
		if (hp != null) {
			sessionFactory.getCurrentSession().delete(hp);

			for (AccountListener accountListener : accountListeners) {
				accountListener.accountRemove(account);
			}
		}
	}

	@Override
	public AccountAvailability checkAccountAvailable(final String username, final String email) {
		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery(CHECK_ACCOUNT_AVAILABILITY);
		query.setParameter(0, username);
		query.setParameter(1, email);

		final long[] res = new long[2];
		final List list = query.list();
		for (Object lValue : list) {
			final Object[] o = (Object[]) lValue;
			if (username.equals(o[0])) {
				res[0]++;
			}
			if (email.equals(o[1])) {
				res[1]++;
			}
		}
		return new AccountAvailability(res[1] == 0, res[0] == 0, accountLockManager.isNicknameLocked(username) == null);
	}

	private void checkAccount(final Account account) throws InadmissibleUsernameException, DuplicateAccountException {
		final String reason = accountLockManager.isNicknameLocked(account.getNickname());
		if (reason != null) {
			throw new InadmissibleUsernameException(account, reason);
		}

		final AccountAvailability a = checkAccountAvailable(account.getNickname(), account.getEmail());
		if (!a.isAvailable()) {
			if (!a.isEmailAvailable() && a.isUsernameAvailable()) {
				throw new DuplicateAccountException(account, "email");
			} else if (a.isEmailAvailable() && !a.isUsernameAvailable()) {
				throw new DuplicateAccountException(account, "username");
			} else {
				throw new DuplicateAccountException(account, "username", "email");
			}
		}
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setAccountLockManager(AccountLockManager accountLockManager) {
		this.accountLockManager = accountLockManager;
	}
}
